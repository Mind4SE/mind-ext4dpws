<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE target PUBLIC "-//mind.ow2.org//Target Descriptor//EN" "classpath://org/ow2/mind/target/ast/targetDescriptor.dtd">
<target name="FoxBoardARM">
<compiler path="cris-axis-linux-gnu-gcc"/>
<linker path="cris-axis-linux-gnu-gcc"/>
<ldFlag id="dpws_ldPath" value="-L${DPWSCORE_ROOT}/platform/gnu/linux/dpws/foxboard"/>
<ldFlag id="al_ldPath" value="-L${DPWSCORE_ROOT}/platform/gnu/linux/al/foxboard"/>
<ldFlag id="dcpl_ldPath" value="-L${DPWSCORE_ROOT}/platform/gnu/linux/dcpl/foxboard"/>
<ldFlag id="xmltools_ldPath" value="-L${DPWSCORE_ROOT}/platform/gnu/linux/xmltools/foxboard"/>
<ldFlag id="gsoap_ldPath" value="-L${DPWSCORE_ROOT}/platform/gnu/linux/gsoap-2.7.6/foxboard"/>
<ldFlag id="common_ldPath" value="-L${DPWSCORE_ROOT}/platform/gnu/linux/common/foxboard"/>
</target>