#include <stdio.h>
#include "tests_common.h"


static struct structB b;
static struct structBB bb;

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

void METH(service_b,test01)(struct structB a,struct structB b, const char **outmsga, const char **outmsgb, const char **outmsgc, const char **inoutmsga, const char**inoutmsgb, const char**inoutmsgc){

	printf("serve service_b :  test01\n");

	printf("a->msg = %s :\n",a.msg);
	printf("a->size = %d :\n",a.size);
	printf("a->time = %d :\n",a.time);

	printf("b->msg = %s :\n",b.msg);
	printf("b->size = %d :\n",b.size);
	printf("b->time = %d :\n",b.time);

	*outmsga = HELLOA;
	*outmsgb = HELLOB;
	*outmsgc = HELLOC;

	printf("inoutmsga = %s :\n",*inoutmsga);
	printf("inoutmsgb = %s :\n",*inoutmsgb);
	printf("inoutmsgc = %s :\n",*inoutmsgc);

	*inoutmsga = BYEA;
	*inoutmsgb = BYEB;
	*inoutmsgc = BYEC;



}

void METH(service_b,test03)(struct structB in){
	printf("serve service_b :  test03\n");
	printf("in.msg = %s\n",in.msg);
	printf("in.size = %d\n",in.size);
	printf("in.time = %d\n",in.time);


}
void METH(service_b,test04)(struct structC in){
	printf("serve service_b :  test04\n");

	printf("in.msg = %s\n",in.msg);
	printf("in.b.msg = %s\n",in.b->msg);
	printf("in.b.size = %d\n",in.b->size);
	printf("in.b.time = %d\n",in.b->time);
	printf("in.bb.msg = %s\n",in.bb->msg);
	printf("in.bb.size = %d\n",in.bb->size);
	printf("in.bb.time = %d\n",in.bb->time);

}

void METH(service_b,test06)(enum couleur c)
{
	printf("serve service_b :  test06\n");
	printColor(c);

}


void METH(service_b,test08)(enum couleur c1, enum couleur c2, enum couleur c3,enum couleur c4)
{
	printf("serve service_b :  test08\n");

	printColor(c1);
	printColor(c2);
	printColor(c3);
	printColor(c4);

}

