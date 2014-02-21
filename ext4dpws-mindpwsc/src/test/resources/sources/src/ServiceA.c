#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "tests_common.h"
#include "testsConst.h"


static void printColor(enum couleur c)
{
	switch(c)
	{
	case vert :
		printf("client sent %s color\n","vert");
		break;
	case jaune :
		printf("client sent %s color\n","jaune");
		break;
	case rouge :
		printf("client sent %s color\n","rouge");
		break;
	default :
		printf("client sent %s color\n","unknown color");
		break;
	}
}

void METH(service_a,test01)(){

}

void METH(service_a,test02)(int a){

	printf("a = %d expected %d\n",a,INT_A);
	if(a != INT_A)
	{
		printf("FAIL\n");
		exit(EXIT_FAILURE);
	}

}

void METH(service_a,test03)(int *a){
	*a = INT_A;
	printf("serve service_a test3\n");

}

void METH(service_a,test04)(int *a)
																																						{
	printf("expected a = %d, resulta = %d\n",3500,a);
	if(*a != 3500){
		printf("FAILED\n");
		exit(EXIT_FAILURE);
	}
	*a = INT_A;

																																						}

void METH(service_a,test05)(int *a, int *b, int *c){

	*a = INT_A;
	*b = INT_B;
	*c = INT_C;

}

void METH(service_a,test06)(int * a,int b,int * c){

	printf("expected(a = %d,b = %d), result(a = %d,b = %d)\n",500,200,a,b);

	if(*a != 500 || b != 200)
	{
		printf("FAILED\n");
		exit(EXIT_FAILURE);
	}
	*c = INT_C;
	*a = INT_A;

}

void METH(service_a,test07)(MindArrayOfInt *array){

	array->size = ARRAY_SIZE;
	array->data = (int *)int_array;

}

void METH(service_a,test08)(MindArrayOfInt *array){

	int i = 0;
	if(array->size != ARRAY_SIZE)
	{
		printf("FAIL array size = '%d' expected size = '%d'\n",array->size,ARRAY_SIZE);
	}
	for(i=0;i<array->size;i++)
	{
		printf("inout array [%d] = %d, expected %d\n",i,array->data[i],int_array[i]);

		if(array->data[i]!=int_array[i])
		{
			printf("FAIL\n");
			exit(EXIT_FAILURE);
		}
	}
	array->data = (int *)reverse_int_array;
	array->size = ARRAY_SIZE;

}

void METH(service_a,test09)(MindArrayOfInt *inoutarray, MindArrayOfInt *outarray){

	int i = 0;
	if(inoutarray->size != ARRAY_SIZE)
	{
		printf("FAIL inoutarray size = '%d' expected size = '%d'\n",inoutarray->size,ARRAY_SIZE);
	}
	for(i=0;i<inoutarray->size;i++)
	{
		printf("inout array [%d] = %d, expected %d\n",i,inoutarray->data[i],int_array[i]);

		if(inoutarray->data[i]!=int_array[i])
		{
			printf("FAIL\n");
			exit(EXIT_FAILURE);
		}
	}
	inoutarray->data = (int *)reverse_int_array;
	inoutarray->size = ARRAY_SIZE;
	outarray->data = (int *)out_int_array;
	outarray->size = ARRAY_SIZE;

}

void METH(service_a,test10)(double *a){

	*a = DOUBLE_A;

}

void METH(service_a,test11)(int *a,int *b){

	int tmp = *a;
	*a = *b;
	*b = tmp;


}

void METH(service_a,test12)(short *a, short *b) {

	*a = INT_B;
	*b = INT_A;

}

void METH(service_a,test13)(const char *msg) {

	printf("client says : %s\n",msg);
	struct structB b;
	b.msg = msg;
	b.size = 2012;
	b.time = 2222;
	ext4dpws_error dpws_error;
	CALL(itf_b,test03)(b,&dpws_error);
}

void METH(service_a,test15)(const char**msg) {
	*msg = HELLOB;

}

void METH(service_a,test16)(struct structA a) {

	printf("structA->msg = %s :\n",a.msg);
	printf("structA->size = %d :\n",a.size);

}

void METH(service_a,test17)(struct structA a, struct structA b) {

	printf("a->msg = %s :\n",a.msg);
	printf("a->size = %d :\n",a.size);
	printf("b->msg = %s :\n",b.msg);
	printf("b->size = %d :\n",b.size);
	if(strcmp(a.msg,HELLOA)!=0
			||strcmp(b.msg,HELLOB)!=0
			||b.size!=20
			||a.size!=10){
		printf("FAIL\n");
		exit(EXIT_FAILURE);

	}

}

void METH(service_a,test18)(const char **msga, const char **msgb, const char **msgc){
	*msga = BYEA;
	*msgb = BYEB;
	*msgc = BYEC;

}

void METH(service_a,test20)(const char ** msg){

	printf("inmsg = %s \n",*msg);
	if(strcmp(*msg,HELLOC)!=0)
	{
		printf("FAIL\n");
		exit(EXIT_FAILURE);
	}
	*msg = BYEC;

}

void METH(service_a,test21)(MindArrayOfInt array){
	int i;
	if(array.size != ARRAY_SIZE)
	{
		printf("FAIL array size = '%d' expected size = '%d'\n",array.size,ARRAY_SIZE);
	}
	for(i=0;i<array.size;i++)
	{
		printf("array [%d] = %d, expected %d\n",i,array.data[i],int_array[i]);

		if(array.data[i]!=int_array[i])
		{
			printf("FAIL\n");
			exit(EXIT_FAILURE);
		}
	}


}

void METH(service_a,test22)(const char ** inoutmsg, const char **outmsg, MindArrayOfInt *inoutarray, MindArrayOfInt *outarray){

	int i = 0;
	for(i=0;i<inoutarray->size;i++)
	{
		printf("inout array [%d] = %d\n",i,inoutarray->data[i]);
	}
	inoutarray->data = (int *)reverse_int_array;
	inoutarray->size = ARRAY_SIZE;
	outarray->data = (int *)out_int_array;
	outarray->size = ARRAY_SIZE;

	if(strcmp(*inoutmsg,HELLOA))
	{
		printf("FAILED (test22 expected(inoutmsg = %s), result(inoutmsg = %s)\n",*inoutmsg);
	} else {
		printf("inoutmsg = %s",*inoutmsg);
	}
	*inoutmsg=BYEA;
	*outmsg=HELLOB;

}

void METH(service_a,test23)(MindArrayOfShort array){

	int i = 0;
	if(array.size != ARRAY_SIZE){
		printf("TEST FAIL : array->size = %d,\
				expected %d\n",
				array.size,
				ARRAY_SIZE);
	} else
	{
		for(i=0;i<ARRAY_SIZE;i++)
		{
			printf("array.data[%d] = %d, expected %d\n",i,array.data[i]);
			if(short_array[i] !=array.data[i])
			{
				printf("TEST FAIL\n");
				exit(EXIT_FAILURE);
			}
		}
	}

}

void METH(service_a,test24)(int * outint, MindArrayOfInt *inoutarray, MindArrayOfInt *outarray){

	int i = 0;
	for(i=0;i<inoutarray->size;i++)
	{
		printf("inout array [%d] = %d\n",i,inoutarray->data[i]);
	}
	inoutarray->data = (int *)reverse_int_array;
	inoutarray->size = ARRAY_SIZE;
	outarray->data = (int *)out_int_array;
	outarray->size = ARRAY_SIZE;
	*outint = INT_A;

}

void METH(service_a,test25)(MindArrayOfFloat array)
																																						{
	int i;
	for(i=0;i<array.size;i++)
	{
		printf("array[%d] = %f\n",i,array.data[i]);
	}


																																						}

void METH(service_a,test27)(MindArrayOfChar array){

	char * msg = malloc(sizeof(char)*ARRAY_SIZE);
	int i;
	for(i=0;i<array.size;i++)
	{
		msg[i] = array.data[i];
	}

	printf("msg = %s",msg);
	free(msg);


}

void METH(service_a,test31)(struct structOfArray a){

	int i;

	if(a.int_array->size != ARRAY_SIZE
			|| a.short_array->size != ARRAY_SIZE)
	{
		printf("TEST FAIL : a.int_array->size = %d,a.short_array->size = %d \
				expected a.int_array->size = %d,a.short_array->size = %d\n",
				a.int_array->size,
				a.short_array->size,ARRAY_SIZE,ARRAY_SIZE);
	} else
	{
		for(i=0;i<ARRAY_SIZE;i++)
		{
			printf("a.int_array->data[%d] = %d, expected %d\n",i,a.int_array->data[i],int_array[i]);
			printf("a.short_array->data[%d] = %d, expected %d\n",i,a.short_array->data[i],short_array[i]);
			if(int_array[i] != a.int_array->data[i] || short_array[i] !=a.short_array->data[i])
			{
				printf("TEST FAIL\n");
				exit(EXIT_FAILURE);
			}
		}
	}

}

void METH(service_a,test32)(MindArrayOfLong array){

	int i;
	if(array.size != ARRAY_SIZE)
	{
		printf("TEST FAIL : array.size = %d, expected %s",array.size,ARRAY_SIZE);
		exit(1);
	} else
	{
		for(i=0;i<array.size;i++)
		{
			if(client_long_array[i] != array.data[i])
			{
				printf("TEST FAIL : array[%d] = %f, expected %f\n",i,array.data[i],client_long_array[i]);
				exit(EXIT_FAILURE);
			} else
			{
				printf("array[%d] = %d\n",i,array.data[i]);
			}
		}
	}

}

void METH(service_a,test33)(enum couleur *c1, enum couleur *c2, enum couleur *c3, enum couleur *c4){
	*c1 = vert;
	*c2 = jaune;
	*c3 = rouge;
	*c4 = vert;

}

void METH(service_a,test34)(struct structD d){

	print_color("c1",d.c1);
	print_color("c2",d.c2);
	print_color("c3",d.c3);

}

void METH(service_a,test35)(enum couleur *c1, enum couleur *c2, enum couleur *c3, enum couleur *c4){

	*c1=jaune;
	*c2=vert;
	*c3=vert;
	*c4=rouge;

}

void METH(service_a,test36)(struct structAA aa){

	printf("expected(aa.msg = %s,aa.size = %d, aa.value = %f), \
			result(aa.msg = %s,aa.size = %d, aa.value = %f)\n",HELLOA,33,1.3f,aa.msg,aa.size,aa.value);

	if(strcmp(aa.msg,HELLOA)
			|| aa.size != 33
			|| aa.value != 1.3f)
	{
		printf("FAILED\n");
		exit(EXIT_FAILURE);
	}

}

void METH(service_a,test37)(struct structOfChar cc){


	printf("result cc.m = '%c',cc.i = '%c',cc.n = '%c',cc.d = '%c'|expected(cc.m='m',cc.i='i',cc.n='n',cc.d='d')\n",cc.m,cc.i,cc.n,cc.d);
	if(cc.m!='m' || cc.i!='i' || cc.n!='n' || cc.d!='d')
	{
		printf("FAIL\n");
		exit(EXIT_FAILURE);
	}

}

void METH(service_a,test38)(struct structE ee){

	printf("expected(ee.msg = '%s') ee.msg ='%s'\n",HELLOA,ee.msg);
	printf("expected(ee.b->msg = '%s') ee.b->msg ='%s'\n",HELLOB,ee.b->msg);
	printf("expected(ee.bb->msg = '%s') ee.bb->msg ='%s'\n",HELLOC,ee.bb->msg);
	if(strcmp(ee.msg,HELLOA)!=0
			|| strcmp(ee.b->msg,HELLOB)!=0
			|| strcmp(ee.bb->msg,HELLOC)!=0)
	{
		printf("FAILED\n");
		exit(EXIT_FAILURE);
	}
	printColor(ee.c1);
	printColor(ee.c2);
	printColor(ee.c3);


}
void METH(service_a,test39)(struct structAA * aa){

	aa->msg = "Is this distribution ok from service_a?";
	aa->size=777;
	aa->value = 5.5f;

}

void METH(service_a,test40)(struct structE *ee){


	static struct structB s_b;

	static struct structBB s_bb;
	s_b.msg = "Stephane";
	s_b.size = 1000;
	s_b.time = 20000000;

	s_bb.msg = "Desbat";
	s_bb.size = 1000;
	s_bb.time = 20000000;

	ee->msg = "Grenoble city!";

	ee->b = &s_b;
	ee->bb = &s_bb;
	ee->c1 = vert;
	ee->c2 = jaune;
	ee->c3 = rouge;

}

void METH(service_a,test41)(MindArrayOfString array) {

	int i;
	for(i=0;i<array.size;i++)
	{
		printf("array->[%d] = %s\n",i,array.data[i]);
	}
}

void METH(service_a,test42)(MindArrayOfString *array) {

	array->data = (const char **)&client_string_array;
	array->size = ARRAY_SIZE;

}

void METH(service_a,test43)(struct structA **array){

	int i = 0;
	while(*array!=NULL)
	{

		printf("array[%d]->aa->msg = %s\n",i,(*array)->msg);
		printf("array[%d]->aa->size = %d\n",i,(*array)->size);
		i++;
		array++;
	}
}

void METH(service_a,test44)(MindArrayOfString array_a,MindArrayOfString array_b) {

	int i;
	for(i=0;i<array_a.size;i++)
	{
		printf("array_a->[%d] = %s\n",i,array_a.data[i]);
	}
	for(i=0;i<array_b.size;i++)
	{
		printf("array_b->[%d] = %s\n",i,array_b.data[i]);
	}
}

void METH(service_a,test45)(MindArrayOfString *array) {

	int i;
	for(i=0;i<array->size;i++)
	{
		printf("array->[%d] = %s\n",i,array->data[i]);
		array->data[i] = "roule ma poule!";
	}
}

void METH(service_a,test46)(struct structAA **array){

	int i = 0;
	while(*array!=NULL)
	{

		printf("array[%d]->aa->msg = %s\n",i,(*array)->msg);
		printf("array[%d]->aa->size = %d\n",i,(*array)->size);
		printf("array[%d]->aa->value = %f\n",i,(*array)->value);

		i++;
		array++;
	}
}

void METH(service_a,test47)(struct structOfChar **array){

	int i = 0;
	while(*array!=NULL)
	{

		printf("array[%d]->aa->m = %c\n",i,(*array)->m);
		printf("array[%d]->aa->i = %c\n",i,(*array)->i);
		printf("array[%d]->aa->n = %c\n",i,(*array)->n);
		printf("array[%d]->aa->d = %c\n",i,(*array)->d);

		i++;
		array++;
	}
}

void METH(service_a,test48)(struct structA **a,struct structAA **aa,struct structOfChar **c){

	CALL(service_a,test43)(a);
	CALL(service_a,test46)(aa);
	CALL(service_a,test47)(c);
}

/*void METH(service_a,test49)(struct structA ***a){

	a1.msg = "Stephane";a2.msg = "fan de ";a3.msg = "distribution!";
	a1.size = 2;a2.size = 4;a3.size = 6;
	struct structA **tabA = malloc(sizeof(struct structA**)*4);
	tabA[0] = (struct structA *)&a1;
	tabA[1] = (struct structA *)&a2;
	tabA[2] = (struct structA *)&a3;
	tabA[3] = NULL;
	*a = tabA;

}*/
