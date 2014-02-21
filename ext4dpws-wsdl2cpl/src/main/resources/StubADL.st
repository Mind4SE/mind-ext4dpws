@Singleton
primitive $NAME$ (deviceId,serviceId){

	provides $INTERFACE_DEFINITION$.Stub as depl_unit_itf;	

	attribute string SERVICE_ID = serviceId;

	attribute string DEVICE_UUID = deviceId;

	source {{

#ifndef WITH_NOGLOBAL
#define WITH_NOGLOBAL
#endif
#define SOAP_FMAC3 static
#include "$SERVICE_NS_PREFIX$C.c"
#include "$SERVICE_NS_PREFIX$Client.c"
	}};

	source {{
#include "$SERVICE_NS_PREFIX$Stub.h"
#include "$SERVICE_NS_PREFIX$.nsmap"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "dc/dc_Dpws.h"
#include "dcDCPL_Mem.h"
#include "al_rtos.h"
#include "al_mem.h"

		static struct dpws dpws_stack;
		static short device_proxy = -1;
		static short service_proxy = -1;
		static int init = 0;
	    static int binded = 0;
	    static const char * NOT_BINDED_ERR = "Remote interface not binded!";
	    static al_mutex_t mutex;

		static void rbind (){

			binded = 0;
			if(init)
			{
				//Find device proxy implementing the service to be invoked.
				printf("Looking for deployment unit { %s }\n",ATTR(DEVICE_UUID));
				service_proxy = -1;
				device_proxy = dpws_lookup_by_id(&dpws_stack, ATTR(DEVICE_UUID));

				if(device_proxy != DPWS_ERR_NO_HANDLE_FOUND)
				{
					printf("DEVICE_FOUND!\n");

					printf("Looking for interface { %s }\n",ATTR(SERVICE_ID));

					if (dpws_pin_proxy(device_proxy) != DPWS_OK) 
					{

						printf("Cannot pin device_proxy %d!\n",device_proxy);
						dpws_release_proxy(device_proxy);
						dpws_end(&dpws_stack);
					} else 
					{

						dpws_end(&dpws_stack);

						service_proxy = dpws_get_service_by_id(&dpws_stack,device_proxy, (char*)ATTR(SERVICE_ID));

						if(service_proxy !=-1)
						{
							printf("SERVICE FOUND!\n");	
							if (dpws_pin_proxy(service_proxy) != DPWS_OK)
							{
								printf("Cannot pin service_proxy %d!\n",service_proxy);
								dpws_release_proxy(device_proxy);
								dpws_release_proxy(service_proxy);
							} else {
								binded = 1;
							}
							dpws_end(&dpws_stack);
						}
						else
						{
							printf("Cannot found service with ID {%s}\n",ATTR(SERVICE_ID));		
							dpws_release_proxy(device_proxy);
						}		
					}
				}else
				{
					printf("Cannot found device with ID {%s}\n",ATTR(DEVICE_UUID));	
					dpws_end(&dpws_stack);			
				}
			} 

		}

		CONSTRUCTOR()
		{
			//init the dpws stack
			int err;
			if((err=dpws_init()) == DPWS_OK)
			{				
				//init the dpws client stack			
				if((err=dpws_client_init(&dpws_stack, NULL)) == DPWS_OK)
				{
					printf ("DPWS client init done!\n");
					err = al_mutex_init(&mutex,"dpws mutex");
					if(err!=AL_SUCCESS)
					{
						printf ("DPWS client mutex init failed!\n");
					
					} else {
						init= 1;		
						rbind();
					}
						
				} else 
				{
					fprintf(stderr, "Could not initialize client side of the DPWSCore stack (err %d)\n", init);
				} 
			} else 
			{
				fprintf(stderr, "Could not initialize the DPWSCore stack (err %d)\n\n", err);		
			}			
		}

		DESTRUCTOR()
		{
			al_mutex_delete(&mutex);
			
		}

		static int getServiceEndPoint(struct wsa_endpoint_ref ** endPoint){

			int ret = DPWS_OK;
			if (dpws_check_proxy(service_proxy)==DC_TRUE)
			{
				*endPoint = dpws_get_default_endpoint_reference(&dpws_stack, service_proxy);

			} else {

				rbind();
				if(binded)
				{
				  *endPoint =  dpws_get_default_endpoint_reference(&dpws_stack, service_proxy);				  
				} else {
					ret = -1;
				}
			}
			return ret;
		}
		$METHS$
	}};
}