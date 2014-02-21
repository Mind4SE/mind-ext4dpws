int $PREFIX$__$METH_NAME$(struct dpws*dpws $DPWS_PARAMS$)
{
	printf("*** SERVE %s ***\n","$METH_NAME$");
	$STRUCT_MEMCPY$
	CALL($ITF_NAME$,$METH_NAME$)($CALL_PARAMS$);
	$SET_OUT_PARAM$	
	$SET_RETURN_PARAM$
	return DPWS_OK;
}