FROM appiriodevops/tc-logstash:member-challenge

COPY credentials /root/.aws/
COPY config /root/.aws/

COPY regions.xml /root/
COPY logstash.lib.sh /opt/logstash/bin/logstash.lib.sh

COPY mapping.sh /root
RUN chmod +x /root/mapping.sh
