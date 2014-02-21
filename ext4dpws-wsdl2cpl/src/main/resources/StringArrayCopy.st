if($ARRAY_FROM$!=NULL && $ARRAY_FROM$->__size >0)
{
	$ARRAY_TO$->data = ($TYPE$*) dcpl_malloc(sizeof($TYPE$) * $ARRAY_FROM$->__size);
	$ARRAY_TO$->size = $ARRAY_FROM$->__size;
	//unsigned int n = $ARRAY_FROM$->__size;
	while($ARRAY_FROM$->__size--)
		$ARRAY_TO$->data[$ARRAY_FROM$->__size] = (const char *)dcpl_strdup((const char *)$ARRAY_FROM$->__ptr[$ARRAY_FROM$->__size]);
}

