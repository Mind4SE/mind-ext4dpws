#include <stdio.h>

// -----------------------------------------------------------------------------
// Implementation of the boot interface.
// -----------------------------------------------------------------------------

// int main(int argc, string[] argv)
int METH(main, main) (int argc, char *argv[]){

	int c;
		printf("Type q or Q to quit, s or S to send hello world\n");
		ext4dpws_error dpws_error;
		do
		{
			c = getchar();
			if(c=='s' || c=='S')
			{
			  CALL(sa, print)("hello world",&dpws_error);
			  CALL(sa, println)("hello world",&dpws_error);
			 }
		}
		while (c != 'q' && c != 'Q');

	  return 0;
}
