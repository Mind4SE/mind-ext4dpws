<wsdl:binding name="$NAME$SoapBinding" type="$PREFIX$:$NAME$PortType">
        <soap:binding style="rpc" transport="http://schemas.xmlsoap.org/soap/http" />
        $BINDING_OPERATION$        
</wsdl:binding>