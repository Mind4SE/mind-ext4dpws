#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "tests_common.h"
#include "testsConst.h"

static ext4dpws_error dpws_error;

void METH(test01)()
{

	struct structB a;
	struct structB b;
	a.msg = HELLOA;
	a.size = 10;
	b.msg = HELLOB;
	b.size = 20;

	char *outmsga = NULL;
	char *outmsgb = NULL;
	char *outmsgc = NULL;
	char *inoutmsga = HELLOA;
	char *inoutmsgb = HELLOB;
	char *inoutmsgc = HELLOC;

	CALL(itf_b,test01)(a,b,(const char**)&outmsga,(const char**)&outmsgb,(const char**)&outmsgc,(const char**)&inoutmsga,(const char**)&inoutmsgb,(const char**)&inoutmsgc,&dpws_error);

	fail_if (dpws_error.err,dpws_error.msg);

	if(!dpws_error.err)
	{
		fail_if (strcmp (outmsga, HELLOA) != 0,
				"outmsga is '%s', expected '%s'",outmsga,HELLOA);
		fail_if (strcmp (outmsgb, HELLOB) != 0,
				"outmsgb is '%s', expected '%s'",outmsgb,HELLOB);
		fail_if (strcmp (outmsgc, HELLOC) != 0,
				"outmsgc is '%s', expected '%s'",outmsgc,HELLOC);

		fail_if (strcmp (inoutmsga, BYEA) != 0,
				"inoutmsga is '%s', expected '%s'",inoutmsga,BYEA);
		fail_if (strcmp (inoutmsgb, BYEB) != 0,
				"inoutmsgb is '%s', expected '%s' ",inoutmsgb,BYEB);
		fail_if (strcmp (inoutmsgc, BYEC) != 0,
				"inoutmsgc is '%s', expected '%s'",inoutmsgc,BYEC);

		printf("outmsga = %s :\n",outmsga);
		printf("outmsgb = %s :\n",outmsgb);
		printf("outmsgc = %s :\n",outmsgc);

		printf("inoutmsga = %s :\n",inoutmsga);
		printf("inoutmsgb = %s :\n",inoutmsgb);
		printf("inoutmsgc = %s :\n",inoutmsgc);

		free(outmsga);
		free(outmsgb);
		free(outmsgc);
		free(inoutmsga);
		free(inoutmsgb);
		free(inoutmsgc);
	}
}


void METH(test02)()
{

}


void METH(test03)()
{
	struct structB in;
	in.msg = HELLOB;
	in.size = SIZE;
	in.time = TIME;
	CALL(itf_b,test03)(in,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);

}


void METH(test04)()
{
	struct structB b;
	b.size = SIZE;
	b.time = TIME;
	b.msg = HELLOB;

	struct structBB bb;
	bb.size = SIZE;
	bb.time = TIME;
	bb.msg = BYEB;

	struct structC c;
	c.msg = HELLOC;
	c.b = &b;
	c.bb = &bb;

	error = CALL(itf_b,test04)(c,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
}


void METH(test05)()
{

}
END_TEST

void METH(test06)()
{
	enum couleur c;
	c = jaune;
	error = CALL(itf_b,test06)(c,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);

}


void METH(test07)()
{

}


void METH(test08)()
{

	enum couleur c1 = rouge;
	enum couleur c2 = jaune;
	enum couleur c3 = vert;
	enum couleur c4 = vert;

	error = CALL(itf_b,test08)(c1,c2,c3,c4,&dpws_error);
	fail_if (dpws_error.err,dpws_error.msg);
}


