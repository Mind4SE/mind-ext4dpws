int $PREFIX$__$METH_NAME$(struct dpws*dpws $DPWS_PARAMS$ ,struct $PREFIX$__$METH_NAME$Response *$PREFIX$__$METH_NAME$ResponseParam)
{
	printf("*** SERVE %s ***\n","$METH_NAME$");
	$STRUCT_MEMCPY$
	CALL($ITF_NAME$,$METH_NAME$)($CALL_PARAMS$);
	$OUT_DATA_COPY$
	return DPWS_OK;
}