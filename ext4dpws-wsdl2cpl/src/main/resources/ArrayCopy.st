if($ARRAY_FROM$!=NULL && $ARRAY_FROM$->__size >0)
{
	$ARRAY_TO$->data = ($TYPE$*) dcpl_malloc(sizeof($TYPE$) * $ARRAY_FROM$->__size);
	memcpy($ARRAY_TO$->data,$ARRAY_FROM$->__ptr,sizeof($TYPE$) * $ARRAY_FROM$->__size);
	$ARRAY_TO$->size = $ARRAY_FROM$->__size;
}
