[![Build Status](https://travis-ci.org/onegang/metrics.svg?branch=master)](https://travis-ci.org/onegang/metrics)

# metrics
Used to generate metrics data.

## Logstash config:
```
# Sample Logstash configuration for creating a simple
# Beats -> Logstash -> Elasticsearch pipeline.

input {
  tcp {
    port => 5000
    codec => json_lines
  }
}
filter {
  mutate {
    convert => { "eventTime" => "string" }
  }
  date {
    match => ["eventTime", "yyyy-MM-dd'T'HH:mm:ssZ"]
    target => "@timestamp"
  }
}
output {
  elasticsearch {
    hosts => ["http://localhost:9200"]
    index => "grapes-%{+YYYY.MM}"
  }
}
```
