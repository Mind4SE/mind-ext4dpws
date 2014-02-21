void METH(depl_unit_itf,$METH_NAME$)($METH_PARAMS$ ext4dpws_error *dpws_error){

	//printf("*** CALL %s ***\n","$METH_NAME$");
	
	al_mutex_lock(&mutex);
	struct wsa_endpoint_ref * serviceEndPoint =  NULL;
	$OUT_DATA_DECL$
	$STRUCT_MEMCPY$
	int ret = getServiceEndPoint(&serviceEndPoint);
	struct dpws * dpws = &dpws_stack;

	if(ret!=-1)
	{	
		ret = dpws_call_$PREFIX$__$METH_NAME$(dpws, serviceEndPoint,NULL,$DPWS_PARAMS$);	
		
		struct soap * soap_s = dpws_dpws2soap(dpws);
		if(ret == DPWS_OK)
		{	
			dpws_error->err = DPWS_OK;
			dpws_error->msg = "OK";
			//deep copy of out data
			$OUT_DATA_COPY$
		} else 
		{
			if (soap_s->error)
			{
				dpws_error->err = soap_s->error;
			}else if(dpws->err)
			{
				dpws_error->err = dpws->err;
			}
		
			dpws_error->msg = dpws_get_error_msg(dpws);			
		}
		dpws_pin_proxy(service_proxy); 				
		dpws_end(dpws);
	} else 
	{
		dpws_error->err = 1;
		dpws_error->msg = NOT_BINDED_ERR;
	}
	
	al_mutex_unlock(&mutex);
	
}