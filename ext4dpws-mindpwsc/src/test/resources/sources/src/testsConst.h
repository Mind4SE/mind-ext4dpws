#ifndef TESTS_CONST_H
#define TESTS_CONST_H
#include <stdarg.h>
#include <stdlib.h>
static void fail_if(int result, const char *expr, ...);
static void controlFloatArray(MindArrayOfFloat *array);
static void controlShortArray(MindArrayOfShort *array);
static void controlIntArray(MindArrayOfInt *array);
static void controlCharArray(MindArrayOfChar *array);
static void controlLongArray(MindArrayOfLong *array);
static void print_color(const char * param, enum couleur c);

static void fail_if (int fail, const char *expr, ...)
{

	const char *msg;

	if (fail) {
		va_list ap;
		char buf[64];

		va_start(ap,expr);
		msg = (const char*)va_arg(ap, char *);
		if (msg == NULL)
		{
			msg = expr;

		}
		vsnprintf(buf, 64, msg, ap);
		va_end(ap);
		printf("Error : %s\n",buf);
		exit(1);
	}
}
static void controlFloatArray(MindArrayOfFloat *array)
{

	fail_if (array->data[0]!=18,"array [%d] = %f, expected %f\n",0,array->data[0],18);
	fail_if (array->data[1]!=-16.23f,"array [%d] = %f, expected %f\n",1,array->data[1],-16.23f);
	fail_if (array->data[2]!=-14.14f,"array [%d] = %f, expected %f\n",2,array->data[2],-14.14f);
	fail_if (array->data[3]!=-12.12f,"array [%d] = %f, expected %f\n",3,array->data[3],-12.12f);
	fail_if (array->data[4]!=10.10f,"array [%d] = %f, expected %f\n",4,array->data[4],10.10f);
	fail_if (array->data[5]!=8.8f,"array [%d] = %f, expected %f\n",5,array->data[5],8.8f);
	fail_if (array->data[6]!=6.6f,"array [%d] = %f, expected %f\n",6,array->data[6],6.6f);
	fail_if (array->data[7]!=4.4f,"array [%d] = %f, expected %f\n",7,array->data[7],4.4f);
	fail_if (array->data[8]!=2.2f,"array [%d] = %f, expected %f\n",8,array->data[8],2.2f);
	fail_if (array->data[9]!=0,"array [%d] = %f, expected %f\n",9,array->data[9],0);
}

static void controlShortArray(MindArrayOfShort *array)
{

	fail_if (array->data[0]!=-18,"array [%d] = %d, expected %d\n",0,array->data[0],-18);
	fail_if (array->data[1]!=-16,"array [%d] = %d, expected %d\n",1,array->data[1],-16);
	fail_if (array->data[2]!=-14,"array [%d] = %d, expected %d\n",2,array->data[2],-14);
	fail_if (array->data[3]!=-12,"array [%d] = %d, expected %d\n",3,array->data[3],-12);
	fail_if (array->data[4]!=10,"array [%d] = %d, expected %d\n",4,array->data[4],10);
	fail_if (array->data[5]!=8,"array [%d] = %d, expected %d\n",5,array->data[5],8);
	fail_if (array->data[6]!=6,"array [%d] = %d, expected %d\n",6,array->data[6],6);
	fail_if (array->data[7]!=4,"array [%d] = %d, expected %d\n",7,array->data[7],4);
	fail_if (array->data[8]!=2,"array [%d] = %d, expected %d\n",8,array->data[8],2);
	fail_if (array->data[9]!=0,"array [%d] = %d, expected %d\n",9,array->data[9],0);
}


static void controlIntArray(MindArrayOfInt *array)
{

	fail_if (array->data[0]!=18,"array [%d] = %d, expected %d\n",0,array->data[0],18);
	fail_if (array->data[1]!=16,"array [%d] = %d, expected %d\n",1,array->data[1],16);
	fail_if (array->data[2]!=14,"array [%d] = %d, expected %d\n",2,array->data[2],14);
	fail_if (array->data[3]!=12,"array [%d] = %d, expected %d\n",3,array->data[3],12);
	fail_if (array->data[4]!=10,"array [%d] = %d, expected %d\n",4,array->data[4],10);
	fail_if (array->data[5]!=8,"array [%d] = %d, expected %d\n",5,array->data[5],8);
	fail_if (array->data[6]!=6,"array [%d] = %d, expected %d\n",6,array->data[6],6);
	fail_if (array->data[7]!=4,"array [%d] = %d, expected %d\n",7,array->data[7],4);
	fail_if (array->data[8]!=2,"array [%d] = %d, expected %d\n",8,array->data[8],2);
	fail_if (array->data[9]!=0,"array [%d] = %d, expected %d\n",9,array->data[9],0);
}

static void controlCharArray(MindArrayOfChar *array)
{

	fail_if (array->data[0]!='0',"array [%d] = '%c', expected '%c'\n",0,array->data[0],'0');
	fail_if (array->data[1]!='1',"array [%d] = '%c', expected '%c'\n",1,array->data[1],'1');
	fail_if (array->data[2]!='2',"array [%d] = '%c', expected '%c'\n",2,array->data[2],'2');
	fail_if (array->data[3]!='3',"array [%d] = '%c', expected '%c'\n",3,array->data[3],'3');
	fail_if (array->data[4]!='4',"array [%d] = '%c', expected '%c'\n",4,array->data[4],'4');
	fail_if (array->data[5]!='5',"array [%d] = '%c', expected '%c'\n",5,array->data[5],'5');
	fail_if (array->data[6]!='6',"array [%d] = '%c', expected '%c'\n",6,array->data[6],'6');
	fail_if (array->data[7]!='7',"array [%d] = '%c', expected '%c'\n",7,array->data[7],'7');
	fail_if (array->data[8]!='8',"array [%d] = '%c', expected '%c'\n",8,array->data[8],'8');
	fail_if (array->data[9]!='9',"array [%d] = '%'c, expected '%c'\n",9,array->data[9],'9');
}

static void controlLongArray(MindArrayOfLong *array)
{
	fail_if (array->data[0]!=1800,"array [%d] = %d, expected %d\n",0,array->data[0],1800);
	fail_if (array->data[1]!=-1623,"array [%d] = %d, expected %d\n",1,array->data[1],-1623);
	fail_if (array->data[2]!=-1414,"array [%d] = %d, expected %d\n",2,array->data[2],-1414);
	fail_if (array->data[3]!=-1212,"array [%d] = %d, expected %d\n",3,array->data[3],-1212);
	fail_if (array->data[4]!=1010,"array [%d] = %d, expected %d\n",4,array->data[4],1010);
	fail_if (array->data[5]!=88,"array [%d] = %d, expected %d\n",5,array->data[5],88);
	fail_if (array->data[6]!=66,"array [%d] = %d, expected %d\n",6,array->data[6],66);
	fail_if (array->data[7]!=44,"array [%d] = %d, expected %d\n",7,array->data[7],44);
	fail_if (array->data[8]!=22,"array [%d] = %d, expected %d\n",8,array->data[8],22);
	fail_if (array->data[9]!=0,"array [%d] = %d, expected %d\n",9,array->data[9],0);
}

static void compareIntArray(MindArrayOfInt *array, int * values)
{
	int i;
	printf("midn array size == %d\n",array->size);
	for(i = 0; i<array->size;i++)
	{
		printf("compare array [%d] == %d == %d\n",i,array->data[i],values[i]);
		fail_if (array->data[i]!=values[i],"array [%d] = %d, expected %d\n",i,array->data[i],values[i]);
	}

}

static void print_color(const char * param, enum couleur c)
{
	switch(c)
	{
	case vert :
		break;
		printf("%s color value is vert.\n",param);
	case jaune :
		printf("%s color value is jaune.\n",param);
		break;
	case rouge :
		printf("%s color value is rouge.\n",param);
		break;
	default  :
		printf("%s color value is unknown.\n",param);
		break;
	}
}

#endif
