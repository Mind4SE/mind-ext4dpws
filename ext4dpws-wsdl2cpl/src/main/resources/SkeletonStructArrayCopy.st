$ARRAY_TO$ = (struct $NAME$ **) dcpl_malloc(sizeof(struct $NAME$*) * $ARRAY_FROM$->__size + 1);
$ARRAY_TO$ [$ARRAY_FROM$->__size] = NULL;

if($ARRAY_FROM$!=NULL && $ARRAY_FROM$->__size >0)
{
	while($ARRAY_FROM$->__size--)
		$ARRAY_TO$[$ARRAY_FROM$->__size] = (struct $NAME$ *)$ARRAY_FROM$->__ptr[$ARRAY_FROM$->__size];
}

