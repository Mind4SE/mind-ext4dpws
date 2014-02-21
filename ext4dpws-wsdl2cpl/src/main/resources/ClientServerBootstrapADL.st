primitive $NAME$.Bootstrap {

	provides boot.Main as main;

	requires optional boot.Main as entryPoint;

	requires optional dpws.SkeletonInterface as skItf[$SKELETONS_SIZE$];

	attribute string DEVICE_UUID = "$DEVICE_UUID$";

	attribute string SCOPE = "http://minalogic.net/project/mind";

	attribute string FRIENDLY_NAME = "Mind-DeploymentUnit($UNIT_NAME$)";

	source  {{

#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "al_rtos.h"
#include "dc/dc_Dpws.h"
#define MY_PRIORITY 0
#define MY_STACK_BASE NULL
#define MY_STACK_SIZE 0
#define BOOT_SEQ_FILE "bootseq.log"	// The name of the file holding the boot sequence number.

		static void runServer(void*);
		static struct qname MindDeviceType = {"http://minalogic.net/projects/mind","MindDevice"};
		static short hDevice;
		struct dpws dpws_stack;
		static int status;
		static al_task_t hThread = {0};

		CONSTRUCTOR()
		{

			printf("<MINDEXT4DPWS initialize DPWS stack...>\n");
			status = dpws_init();
			if (status != DPWS_OK)
			{
				fprintf(stderr, "Could not initialize the DPWSCore stack (err %d)\n\n", status);
				exit(0);
			}

		}

		DESTRUCTOR()
		{

		}

		int METH(readintfromfile)(const char* filename) {
			FILE* f = fopen(filename, "r");
			char buf[100];
			int val;
			size_t n;
			if (!f || (n = fread(buf, 1, 99, f)) <= 0) {
				val = 0;
			} else {
				buf[n] = '\0';
				sscanf(buf, "%d", &val);
			}
			if (f) fclose(f);
			return val;
		}

		int METH(writeinttofile)(int val, const char* filename) {
			FILE* f = fopen(filename, "w");
			char buf[100];
			sprintf(buf, "%d", val);
			fwrite(buf, 1, strlen(buf), f);
			fclose(f);
			return val;
		}

		void METH(print_device_info)(){

			int len = 10, sz, i, j;
			short hrefs[10];
			char ** transportAddrs = NULL;
			// One service port (HTTP binding) created by default but potentially multiple
			// local addresses (multiple network interfaces & IP versions)
			transportAddrs = dpws_get_transport_addresses(dpws_get_default_service_port(hDevice), &sz);
			printf("\n+ Device [%s] started\n\nAddresses:\n", dpws_get_ptr_att(hDevice, DPWS_STR_DEVICE_ID));
			for (i = 0; i < sz; i++)
			{
				printf("[%d] %s\n", i, transportAddrs[i]);
				dpws_free(transportAddrs[i]);	// because of dpws_get_transport_addresses
			}
			dpws_free(transportAddrs);	// because of dpws_get_transport_addresses
			if (!dpws_get_service_handles(hDevice, hrefs, &len))
			{
				printf("\n++ Hosted services:\n\n");
				for (i = 0; i < len; i++)
				{
					transportAddrs = dpws_get_transport_addresses(dpws_get_default_service_port(hrefs[i]), &sz);	// same as for device
					printf("Service ID: %s\nAddresses:\n", dpws_get_ptr_att(hrefs[i], DPWS_STR_SERVICE_ID));
					for (j = 0; j < sz; j++) {
						printf("[%d] %s\n", j, transportAddrs[j]);
						dpws_free(transportAddrs[j]);	// because of dpws_get_transport_addresses
					}
					dpws_free(transportAddrs);	// because of dpws_get_transport_addresses
				}
				dpws_release_handles(hrefs, len);
			}
		}


		/* Single thread message listener */
		static void runServer(void *dpws_) {

			struct dpws *dpws = (struct dpws *)dpws_;
			status = DPWS_OK;
			while (1)
			{
				status = dpws_accept(dpws);	// monitors listening sockets for incoming messages
				if (status)
				{
					if (status != DPWS_ERR_SERVER_STOPPED)
					{// The only status that is not really an error and allows normal exit of the server loop
						fprintf(stderr, dpws_get_error_msg(dpws));
					}
					goto exit;
				}
				if (dpws_serve(dpws))
				{	// performs message processing.
					fprintf(stderr, dpws_get_error_msg(dpws));	// Better thing to do on an embedded server is logging...
				}
				dpws_end(dpws);	// frees transient message memory.
			}
			exit:
			printf("Exit from server loop\n");
			dpws_server_shutdown(dpws);	// Clean the DPWS registry.
			dpws_shutdown();	// Clean the stack.
		}



		/* Start server message listener */
		int METH(bootServer)()
		{

			return al_task_create (&hThread,       /* Thread handle */
					"aThread",      /* thread name */
					runServer,      /* entry function */
					&dpws_stack,           /* parameter */
					MY_PRIORITY,    /* priority */
					MY_STACK_BASE,  /* start of stack */
					MY_STACK_SIZE   /* size of stack */);
		}


		/* Stops the server */
		void METH(stopServer)(){
			dpws_stop_server(2000);	// shutdowns the dpws listener and process all pending operations
			if(hThread!=0)
			{
#ifdef __WIN32__

				WaitForSingleObject(hThread,INFINITE);
#else //POSIX
				pthread_join(hThread,NULL);

#endif
			}
		}

		int METH(registerService)(){

			int i;
			struct prefixed_qname *portType = NULL;
			struct wsdl_info *wsdlInfo = NULL;
			void * cbk = NULL;
			const char *serviceId = NULL;
			short hServClass = -1;
			short hSPServPort = -1;
			short hSPServ = -1;

			for(i=0;i<$SKELETONS_SIZE$;i++)
			{

				portType = (struct prefixed_qname*)CALL(skItf[i],getPortType)();
				wsdlInfo = (struct wsdl_info *)CALL(skItf[i],getWSDLInfo)();
				cbk = CALL(skItf[i],getCallBack)();
				serviceId = CALL(skItf[i],getServiceId)();
				hServClass =  dpws_create_service_class();

				DPWS_ADD_PTR_ATT(hServClass, DPWS_PTR_PREFIXED_TYPE, portType);
				DPWS_ADD_PTR_ATT(hServClass, DPWS_PTR_WSDL, wsdlInfo);
				DPWS_ADD_PTR_ATT(hServClass, DPWS_PTR_HANDLING_FUNCTION, cbk);
				DPWS_SET_STR_ATT(hServClass, DPWS_STR_ID, serviceId);

				hSPServPort = dpws_create_service_port();	// creates a service port (network endpoint)
				DPWS_SET_STR_ATT(hSPServPort, DPWS_STR_ADDRESS, serviceId);	// sets the context path for the service physical address.
				hSPServ = dpws_create_hosted_service(hDevice, hServClass);	// creates the service
				dpws_release_handle(hServClass);	// Now used by the service and not needed anymore
				dpws_bind_service(hSPServ, hSPServPort);	// associates it with a "network endpoint"
				dpws_release_handle(hSPServPort);	// Not needed anymore

			}
			return 1;
		}

		int METH(initDevice)(){


			int c,i;
			char * scope_suffix = NULL;
			struct localized_string ls = {NULL, NULL};
			printf("<MINDEXT4DPWS device initialization...>\n");

			hDevice = dpws_create_custom_device(0, -1);	// the id (0) must be unique in the physical local device

			DPWS_ADD_PTR_ATT(hDevice, DPWS_PTR_TYPE, &MindDeviceType);
			DPWS_SET_INT_ATT(hDevice, DPWS_INT_METADATA_VERSION, 1);
			ls.s = (char *)ATTR(FRIENDLY_NAME);
			DPWS_SET_STR_ATT(hDevice, DPWS_PTR_FRIENDLY_NAME, &ls);
			ls.s = "MindComponent";
			DPWS_SET_STR_ATT(hDevice, DPWS_PTR_MODEL_NAME, &ls);
			ls.s = "Minalogic ";
			DPWS_SET_STR_ATT(hDevice, DPWS_PTR_MANUFACTURER, &ls);
			DPWS_ADD_STR_ATT(hDevice, DPWS_STR_SCOPE, ATTR(SCOPE));
			DPWS_SET_STR_ATT(hDevice, DPWS_STR_FIRMWARE_VERSION, "1.0");
			DPWS_SET_STR_ATT(hDevice, DPWS_STR_SERIAL_NUMBER, "56080001");
			DPWS_SET_STR_ATT(hDevice, DPWS_STR_MODEL_NUMBER, "1.0");
			DPWS_SET_STR_ATT(hDevice, DPWS_STR_MODEL_URL, "http://minalogic.net/project/mind/MindComponent.html");
			DPWS_SET_STR_ATT(hDevice, DPWS_STR_PRESENTATION_URL, "RemoteComponentOverview.html");	// Any relative URI will use the default presentation server
			DPWS_SET_STR_ATT(hDevice, DPWS_STR_MANUFACTURER_URL, "http://minalogic.net/");
			DPWS_SET_STR_ATT(hDevice, DPWS_STR_DEVICE_ID, ATTR(DEVICE_UUID));

			return 1;
		}


		// the main of the skeleton application
		int METH(main, main) (int argc, char *argv[]){


			int c,bootSeq;

			int port = $PORT$;


			bootSeq = CALL(readintfromfile)(BOOT_SEQ_FILE);	// Reading the boot sequence number from a file
			CALL(writeinttofile)(bootSeq + 1, BOOT_SEQ_FILE);	// Writing the next boot sequence number to the same file
			DPWS_SET_INT_ATT(DC_REGISTRY_HANDLE, DPWS_INT_BOOT_SEQ, bootSeq);	// Configure the boot sequence number to use in this session (mandatory).
			DPWS_SET_INT_ATT(DC_REGISTRY_HANDLE, DPWS_INT_HTTP_PORT, port);	// Configure the HTTP listen port.
			CALL(initDevice)();
			CALL(registerService)();
			printf("<MINDEXT4DPWS enable device on network...>\n");
			if ((status = dpws_enable_device(hDevice)))	// Enables device (especially schedule hello messages).
			{
				fprintf(stderr, "Could not execute dpws_enable_device (err %d)\n", status);
				dpws_shutdown();
				exit(1);
			}

			CALL(print_device_info)();	// Print device and services information.
			dpws_release_handle(hDevice);	// Enabling makes it now hold by the registry

			printf("<MINDEXT4DPWS initialize DPWS Server...>\n");

			if ((status = dpws_server_init(&dpws_stack, NULL)))	// initializes the dpws structure for server-side operations.
			{
				fprintf(stderr, "Could not initialize DPWS server (err %d)\n", status);
				dpws_shutdown();
				exit(1);
			}

			printf("<MINDEXT4DPWS boot DPWS Server...>\n");
			status = CALL(bootServer)();
			if (status !=0)
			{	// see listener.c
				fprintf (stderr, "Thread Creation ERROR (%d) - bootServer()\n", status);
				dpws_shutdown();
				exit(1);
			}

			printf("<MINDEXT4DPWS deployment unit is ready!>\n");
			int ret = CALL(entryPoint,main)(argc,argv);//loop on user application
			CALL(stopServer)();
			return ret;
		}

	}};

}
