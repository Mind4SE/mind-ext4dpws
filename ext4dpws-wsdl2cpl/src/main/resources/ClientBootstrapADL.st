primitive $NAME$.Bootstrap {

	provides boot.Main as main;

	requires optional boot.Main as entryPoint;
	
	source  {{

#include <string.h>
#include <stdio.h>
#include <stdlib.h>

		int METH(main, main) (int argc, char *argv[]){

			printf("<MINDEXT4DPWS boostrap calling user main...>\n");

			int ret = CALL(entryPoint,main)(argc,argv);
			
			dpws_shutdown();
			
			return ret;
		}

	}};

}