<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE target PUBLIC "-//mind.ow2.org//Target Descriptor//EN" "classpath://org/ow2/mind/target/ast/targetDescriptor.dtd">
<target name="GumstixARM">
<compiler path="arm-angstrom-linux-gnueabi-gcc"/>
<linker path="arm-angstrom-linux-gnueabi-gcc"/>
<ldFlag id="dpws_ldPath" value="-L${DPWSCORE_ROOT}/platform/gnu/linux/dpws/gumstix"/>
<ldFlag id="al_ldPath" value="-L${DPWSCORE_ROOT}/platform/gnu/linux/al/gumstix"/>
<ldFlag id="dcpl_ldPath" value="-L${DPWSCORE_ROOT}/platform/gnu/linux/dcpl/gumstix"/>
<ldFlag id="xmltools_ldPath" value="-L${DPWSCORE_ROOT}/platform/gnu/linux/xmltools/gumstix"/>
<ldFlag id="gsoap_ldPath" value="-L${DPWSCORE_ROOT}/platform/gnu/linux/gsoap-2.7.6/gumstix"/>
<ldFlag id="common_ldPath" value="-L${DPWSCORE_ROOT}/platform/gnu/linux/common/gumstix"/>
</target>