#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "tests_common.h"
#include "testsConst.h"

void METH(test_a2_01)()
{

	CALL(itf_a2,test01)(&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);


}


void METH (test_a2_02)()
{
	a = INT_A;
	CALL(itf_a2,test02)(a,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);

}


void METH (test_a2_03)()
{
	CALL(itf_a2,test03)(&a,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		fail_if (a!=INT_A,"a=%d expected a=%d\n",a,INT_A);
	}



}


void METH (test_a2_04)()
{
	a = 3500;
	CALL(itf_a2,test04)(&a,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		fail_if (a!=INT_A,"a=%d expected a=%d\n",a,INT_A);
	}



}



void METH (test_a2_05)()
{
	a = 0;
	b = 0;
	c = 0;
	CALL(itf_a2,test05)(&a,&b,&c,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		fail_if (a!=INT_A || b!=INT_B || c!=INT_C,"a=%d,b=%d,c=%d expected a=%d,b=%d,c=%d\n",a,b,c,INT_A,INT_B,INT_C);
	}



}



void METH (test_a2_06)()
{
	a = 500;
	c = 0;
	b = 200;
	CALL(itf_a2,test06)(&a,b,&c,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		fail_if (a!=INT_A || c!=INT_C,"a=%d,c=%d expected a=%d,c=%d\n",a,c,INT_A,INT_C);
	}



}


void METH (test_a2_07)()
{
	MindArrayOfInt array;
	CALL(itf_a2,test07)(&array,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		compareIntArray(&array,(int *)int_array);
	}




}


void METH (test_a2_08)()
{

	MindArrayOfInt array;
	array.data = (int *)int_array;
	array.size = ARRAY_SIZE;
	CALL(itf_a2,test08)(&array,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		compareIntArray(&array,(int *)reverse_int_array);
	}




}


void METH (test_a2_09)()
{
	MindArrayOfInt inoutarray;
	MindArrayOfInt outarray;
	inoutarray.data = (int *)int_array;
	inoutarray.size = ARRAY_SIZE;
	CALL(itf_a,test09)(&inoutarray,&outarray,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		compareIntArray(&inoutarray,(int *)reverse_int_array);
		compareIntArray(&outarray,(int *)out_int_array);
	}


}


void METH (test_a2_10)()
{
	double d = 0;
	CALL(itf_a2,test10)(&d,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		fail_if (d!=DOUBLE_A,"d=%d expected d=%f\n",d,INT_C,DOUBLE_A);
	}



}


void METH (test_a2_11)()
{
	printf("Test 11 : ");
	a = INT_A;
	b = INT_B;
	CALL(itf_a2,test11)(&a,&b,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		fail_if (a!=INT_B || b!=INT_A,"a=%d, b=%d expected a=%d, b=%d\n",a,b,INT_B,INT_A,INT_C);
	}





}


void METH (test_a2_12)()
{
	short a = 0;
	short b = 0;
	CALL(itf_a2,test12)(&a,&b,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		fail_if (a!=INT_B || b!=INT_A,"a=%d, b=%d expected a=%d, b=%d\n",a,b,INT_B,INT_A);
	}



}


void METH (test_a2_13)()
{
	char * msg = HELLOB;
	CALL(itf_a2,test13)(msg,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
}


void METH (test_a2_14)()
{

}


void METH (test_a2_15)()
{
	char * msg;
	CALL(itf_a2,test15)((const char**)&msg,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		fail_if (strcmp (msg, HELLOB) != 0, "msg is '%s', expected '%s'",msg,HELLOB);
		free(msg);
	}

}


void METH (test_a2_16)()
{
	struct structA a;
	a.msg = HELLOA;
	a.size = 10;
	CALL(itf_a2,test16)(a,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);




}


void METH (test_a2_17)()
{
	struct structA a;
	struct structA b;
	a.msg = HELLOA;
	a.size = 10;
	b.msg  = HELLOB;
	b.size = 20;
	CALL(itf_a2,test17)(a,b,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);



}


void METH (test_a2_18)()
{

	char * msga;
	char * msgb;
	char * msgc;
	CALL(itf_a2,test18)((const char**)&msga,(const char**)&msgb,(const char**)&msgc,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		fail_if (strcmp (msga, BYEA) != 0 || strcmp (msgb, BYEB) != 0 || strcmp (msgc, BYEC) != 0 ,
				"msga='%s',msgb='%s',msgc='%s' expected msga='%s',msgb='%s',msgc='%s'",msga,msgb,msgc,BYEA,BYEB,BYEC);
		free(msga);
		free(msgb);
		free(msgc);
	}



}



void METH (test_a2_20)()
{
	char * msg = HELLOC;
	CALL(itf_a2,test20)((const char**)&msg,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		fail_if (strcmp (msg, BYEC) != 0 ,
				"msg='%s' expected msg='%s'",msg,BYEC);
		free(msg);
	}


}


void METH (test_a2_21)()
{
	MindArrayOfInt array;
	array.data = (int *)&int_array;
	array.size = ARRAY_SIZE;
	CALL(itf_a2,test21)(array,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);


}


void METH (test_a2_22)()
{

	MindArrayOfInt inoutarray;
	MindArrayOfInt outarray;
	char * inoutmsg = HELLOA;
	char * outmsg;
	inoutarray.data = (int *)int_array;
	inoutarray.size = ARRAY_SIZE;
	CALL(itf_a2,test22)((const char**)&inoutmsg,(const char**)&outmsg,&inoutarray,&outarray,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		compareIntArray(&inoutarray,(int *)reverse_int_array);
		compareIntArray(&outarray,(int *)out_int_array);
		fail_if((strcmp(inoutmsg,BYEA)!=0)||(strcmp(outmsg,HELLOB)!=0), "inoutmsg=%s, expected = %s, outmsg=%s, expected=%s\n",inoutmsg,BYEA,outmsg,HELLOB);
	}


}


void METH (test_a2_23)()
{

	MindArrayOfShort array;
	array.data = (short *)short_array;
	array.size = ARRAY_SIZE;
	CALL(itf_a2,test23)(array,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);



}


void METH (test_a2_24)()
{

	MindArrayOfInt inoutarray;
	MindArrayOfInt outarray;
	int outint = 0;
	inoutarray.data = (int *)int_array;
	inoutarray.size = ARRAY_SIZE;
	CALL(itf_a2,test24)(&outint,&inoutarray,&outarray,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		compareIntArray(&inoutarray,(int *)reverse_int_array);
		compareIntArray(&outarray,(int *)out_int_array);
		fail_if(outint!=INT_B, "outint=%d, expected = %d\n",outint,INT_A);
	}


}


void METH (test_a2_25)()
{

	MindArrayOfFloat array;
	float data [ARRAY_SIZE]  = {0,-2.2,-4.4,-6,8.8,10.10,12.12,14.14,16.16,18.18};
	array.data = (float *)&data;
	array.size = ARRAY_SIZE;
	CALL(itf_a2,test25)(array,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
}


void METH (test_a2_26)()
{


}


void METH (test_a2_27)()
{

	MindArrayOfChar array;
	char data [ARRAY_SIZE]  = {'h','e','l','l','o','w','o','r','l','d'};
	array.data = (char *)data;
	array.size = ARRAY_SIZE;
	CALL(itf_a2,test27)(array,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);



}


void METH (test_a2_28)()
{


}


void METH (test_a2_29)()
{


}


void METH (test_a2_30)()
{


}


void METH (test_a2_31)()
{

	MindArrayOfInt arrayOfInt;
	arrayOfInt.data = (int *)&int_array;
	arrayOfInt.size = ARRAY_SIZE;

	MindArrayOfShort arrayOfShort;
	arrayOfShort.data = (short *)&short_array;
	arrayOfShort.size = ARRAY_SIZE;

	struct structOfArray sa;
	sa.int_array= &arrayOfInt;
	sa.short_array= &arrayOfShort;
	CALL(itf_a2,test31)(sa,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);



}


void METH (test_a2_32)()
{

	MindArrayOfLong array;
	array.data = (long *)&client_long_array;
	array.size = ARRAY_SIZE;
	CALL(itf_a2,test32)(array,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);



}


void METH (test_a2_33)()
{

	enum couleur c1;
	enum couleur c2;
	enum couleur c3;
	enum couleur c4;
	CALL(itf_a2,test33)(&c1,&c2,&c3,&c4,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		fail_if(c1!=rouge||c2!=jaune || c3!=vert || c4!=rouge,
				"c1=%d,c2=%d,c3=%d,c4=%d,expected c1=%d,c2=%d,c3=%d,c4=%d",c1,c2,c3,c4,rouge,jaune,vert,rouge);
	}


}


void METH (test_a2_34)()
{

	struct structD d;
	d.c1 = vert;
	d.c2 = jaune;
	d.c3 = rouge;
	CALL(itf_a2,test34)(d,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);


}


void METH (test_a2_35)()
{

	printf("Test 35 : ");
	enum couleur c1 = rouge;
	enum couleur c2 = vert;
	enum couleur c3 = jaune;
	enum couleur c4 = rouge;
	CALL(itf_a2,test35)(&c1,&c2,&c3,&c4,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{

		fail_if(c1!=vert||c2!=jaune || c3!=jaune || c4!=rouge,
				"c1=%d,c2=%d,c3=%d,c4=%d,expected c1=%d,c2=%d,c3=%d,c4=%d",c1,c2,c3,c4,vert,jaune,jaune,rouge);

	}

}


void METH (test_a2_36)()
{

	struct structAA aa;
	aa.msg = HELLOA;
	aa.size = 33;
	aa.value = 1.3f;
	CALL(itf_a2,test36)(aa,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);

}


void METH (test_a2_37)()
{

	struct structOfChar cc;
	cc.m = 'm';
	cc.i = 'i';
	cc.n = 'n';
	cc.d = 'd';
	CALL(itf_a2,test37)(cc,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);

}



void METH (test_a2_38)()
{

	struct structE ee;
	struct structB b;
	b.size = SIZE;
	b.time = TIME;
	b.msg = HELLOB;

	struct structBB bb;
	bb.size = SIZE;
	bb.time = TIME;
	bb.msg = HELLOC;

	enum couleur c1 = rouge;
	enum couleur c2 = jaune;
	enum couleur c3 = vert;

	ee.msg = HELLOA;
	ee.b = &b;
	ee.bb = &bb;
	ee.c1 = c1;
	ee.c2 = c2;
	ee.c3 = c3;

	CALL(itf_a2,test38)(ee,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);


}


void METH (test_a2_39)()
{

	struct structAA aa;
	CALL(itf_a2,test39)(&aa,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);

	if(!dpws_error.err)
	{
		printf("aa.msg = %s\n",aa.msg);
		printf("aa.size =  %d\n",aa.size);
		printf("aa.value = %f\n",aa.value);
	}

}


void METH (test_a2_40)()
{

	struct structE ee;
	CALL(itf_a2,test40)(&ee,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);

	if(!dpws_error.err)
	{

		printf("ee.msg = %s\n",ee.msg);

		printf("ee.b->msg = %s\n",ee.b->msg);
		printf("ee.b->size = %d\n",ee.b->size);
		printf("ee.b->value = %d\n",ee.b->time);

		printf("ee.bb->msg = %s\n",ee.bb->msg);
		printf("ee.bb->size = %d\n",ee.bb->size);
		printf("ee.bb->value = %d\n",ee.bb->time);
		fail_if(ee.c1!=vert||ee.c2!=jaune || ee.c3!=rouge,
				"ee.c1=%d,ee.c2=%d,ee.c3=%d,expected c1=%d,c2=%d,c3=%d,",ee.c1,ee.c2,ee.c3,vert,jaune,rouge);


	}

}


void METH (test_a2_41)()
{

	MindArrayOfString array;
	array.data = (const char**)&client_string_array;
	array.size = ARRAY_SIZE;
	CALL(itf_a2,test41)(array,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);

}


void METH (test_a2_42)()
{

	MindArrayOfString array;
	CALL(itf_a2,test42)(&array,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
	if(!dpws_error.err)
	{
		int i;
		for(i=0;i<array.size;i++)
		{
			printf("array->[%d] = %s\n",i,array.data[i]);
		}
	}

}


void METH (test_a2_43)()
{

	struct structA a1;
	struct structA a2;
	struct structA a3;
	a1.msg = "Stephane";
	a2.msg = "fan de ";
	a3.msg = "distribution!";

	a1.size = 2;
	a2.size = 4;
	a3.size = 6;

	struct structA **tab = malloc(sizeof(struct structA*)*4);

	tab[0] = (struct structA *)&a1;
	tab[1] = (struct structA *)&a2;
	tab[2] = (struct structA *)&a3;
	tab[3] = NULL;

	CALL(itf_a2,test43)(tab,&dpws_error);
	free(tab);
	fail_if (dpws_error.err,dpws_error.msg);

}


void METH (test_a2_44)()
{

	MindArrayOfString array_a;
	array_a.data = (const char**)&client_string_array;
	array_a.size = ARRAY_SIZE;

	MindArrayOfString array_b;
	array_b.data = (const char**)&client_string_array;
	array_b.size = ARRAY_SIZE;
	CALL(itf_a2,test44)(array_a,array_b,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);

}


void METH (test_a2_45)()
{

	MindArrayOfString array;
	array.data = (const char**)&client_string_array;
	array.size = ARRAY_SIZE;

	CALL(itf_a2,test45)(&array,&dpws_error);
	int i;
	for(i=0;i<array.size;i++)
	{
		printf("array->[%d] = %s\n",i,array.data[i]);
	}
	fail_if (dpws_error.err,dpws_error.msg);
}

void METH (test_a2_46)()
{

	struct structAA a1;
	struct structAA a2;
	struct structAA a3;
	a1.msg = "Iheb";
	a2.msg = "fan de ";
	a3.msg = "distribution!";

	a1.size = 2;
	a2.size = 4;
	a3.size = 6;

	a1.value = 2.4;
	a2.value = 4.6;
	a3.value = 6.2;

	struct structAA **tab = malloc(sizeof(struct structAA**)*4);

	tab[0] = (struct structAA *)&a1;
	tab[1] = (struct structAA *)&a2;
	tab[2] = (struct structAA *)&a3;
	tab[3] = NULL;

	CALL(itf_a2,test46)(tab,&dpws_error);
	free(tab);
	fail_if (dpws_error.err,dpws_error.msg);

}


void METH (test_a2_47)()
{

	struct structOfChar a1;
	struct structOfChar a2;
	struct structOfChar a3;
	a1.m = 'm';
	a1.i = 'i';
	a1.n = 'n';
	a1.d = 'd';

	a2.m = 'm';
	a2.i = 'i';
	a2.n = 'n';
	a2.d = 'd';

	a3.m = 'm';
	a3.i = 'i';
	a3.n = 'n';
	a3.d = 'd';



	struct structOfChar **tab = malloc(sizeof(struct structOfChar**)*4);

	tab[0] = (struct structOfChar *)&a1;
	tab[1] = (struct structOfChar *)&a2;
	tab[2] = (struct structOfChar *)&a3;
	tab[3] = NULL;

	CALL(itf_a2,test47)(tab,&dpws_error);
	free(tab);
	fail_if (dpws_error.err,dpws_error.msg);

}


void METH (test_a2_48)()
{

	struct structOfChar c1;
	struct structOfChar c2;
	struct structOfChar c3;

	struct structA a1;
	struct structA a2;
	struct structA a3;

	struct structAA aa1;
	struct structAA aa2;
	struct structAA aa3;

	c1.m = 'm';c1.i = 'i';c1.n = 'n';c1.d = 'd';
	c2.m = 'm';c2.i = 'i';c2.n = 'n';c2.d = 'd';
	c3.m = 'm';c3.i = 'i';c3.n = 'n';c3.d = 'd';

	aa1.msg = "Iheb";aa2.msg = "fan de ";aa3.msg = "distribution!";
	aa1.size = 2;aa2.size = 4;aa3.size = 6;
	aa1.value = 2.4;aa2.value = 4.6;aa3.value = 6.2;


	a1.msg = "Stephane";a2.msg = "fan de ";a3.msg = "distribution!";
	a1.size = 2;a2.size = 4;a3.size = 6;

	struct structA **tabA = malloc(sizeof(struct structA**)*4);
	struct structAA **tabAA = malloc(sizeof(struct structAA**)*4);
	struct structOfChar **tabC = malloc(sizeof(struct structOfChar**)*4);

	tabA[0] = (struct structA *)&a1;
	tabA[1] = (struct structA *)&a2;
	tabA[2] = (struct structA *)&a3;
	tabA[3] = NULL;

	tabAA[0] = (struct structAA *)&aa1;
	tabAA[1] = (struct structAA *)&aa2;
	tabAA[2] = (struct structAA *)&aa3;
	tabAA[3] = NULL;

	tabC[0] = (struct structOfChar *)&c1;
	tabC[1] = (struct structOfChar *)&c2;
	tabC[2] = (struct structOfChar *)&c3;
	tabC[3] = NULL;

	CALL(itf_a2,test48)(tabA,tabAA,tabC,&dpws_error);
	free(tabA);
	free(tabAA);
	free(tabC);
	fail_if (dpws_error.err,dpws_error.msg);
}

