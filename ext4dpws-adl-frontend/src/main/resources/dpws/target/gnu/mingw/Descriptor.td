<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE target PUBLIC "-//mind.ow2.org//Target Descriptor//EN" "classpath://org/ow2/mind/target/ast/targetDescriptor.dtd">
<target name="dpws.target.gnu.mingw.Descriptor">
<cFlag id="mingw" value="-I${DPWSCORE_ROOT}/platform/gnu/mingw/include"/>
<cFlag id="al" value="-I${DPWSCORE_ROOT}/abstract_layer/include"/>
<cFlag id="al_rtos" value="-I${DPWSCORE_ROOT}/abstract_layer/targets/rtos"/>
<cFlag id="xml_tools" value="-I${DPWSCORE_ROOT}/src/xmltools/include"/>
<cFlag id="dcpl" value="-I${DPWSCORE_ROOT}/src/dcpl/include"/>
<cFlag id="gsoap" value="-I${DPWSCORE_ROOT}/src/gsoap-2.7.6/include"/>
<cFlag id="dpws" value="-I${DPWSCORE_ROOT}/src/dpws/include"/>
<cFlag id="common" value="-I${DPWSCORE_ROOT}/src/common/include"/>
<cFlag id="dpws_src" value="-I${DPWSCORE_ROOT}/src/dpws/src"/>
<cFlag id="mingw_src" value="-D_GNU_SOURCE "/>
<ldFlag id="dpws_ld" value="-ldpws"/>
<ldFlag id="dcpl_ld" value="-ldcpl"/>
<ldFlag id="al_ld" value="-lal"/>
<ldFlag id="common_ld" value="-lcommon"/>
<ldFlag id="gsoap_ld" value="-lgsoap-2.7.6"/>
<ldFlag id="xmltools_ld" value="-lxmltools"/>
<ldFlag id="ws2_32" value="-lws2_32"/>
<ldFlag id="iphlpapi" value="-liphlpapi"/>
</target>
