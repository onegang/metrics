# metrics
Used to generate metrics data.

## Logstash config:
```
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
    index => "grapes-%{+YYYY.MM.dd}"
  }
}
```
