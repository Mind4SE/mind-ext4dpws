@Singleton
primitive $NAME$(serviceId) {
	
	requires $INTERFACE_DEFINITION$ as depl_unit_itf;
	
	provides dpws.SkeletonInterface as skItf;
	
	attribute string SERVICE_ID = serviceId;
	
	source {{
		#ifndef WITH_NOGLOBAL
		#define WITH_NOGLOBAL
		#endif
		#define SOAP_FMAC3 static
		#include "$SERVICE_NS_PREFIX$C.c"
		#include "$SERVICE_NS_PREFIX$Server.c"		

	}};
	source {{
	
		#include "$SERVICE_NS_PREFIX$Stub.h"
		#include "$SERVICE_NS_PREFIX$.nsmap"
		#include "dcDPWS_Memory.h"
		#include "dcDCPL_Mem.h"
		#include <stdint.h>
		static struct prefixed_qname PORT_TYPE= {{"$SERVICE_NS$","$PREFIX$PortType"},"$SERVICE_NS_PREFIX$"};
	
		static struct wsdl_info WSDL_INFO = {"$SERVICE_NS$", "http://org.ow2.mind.$INTERFACE_DEFINITION$/$SERVICE_NS_PREFIX$.wsdl"};
	
		void *METH(skItf,getCallBack)()
		{
			return $SERVICE_NS_PREFIX$_serve_request;
		}
	
	  	const char *METH(skItf,getServiceId)()
	    {
	    	return ATTR(SERVICE_ID);
	    }
	
	    void *METH(skItf,getPortType)()
	    {
	   		return &PORT_TYPE;
	    }
	
	    void *METH(skItf,getWSDLInfo)()	   
	    {
	   		return &WSDL_INFO;	   		
	    }
		
		
		$CALLS$
		}};
}