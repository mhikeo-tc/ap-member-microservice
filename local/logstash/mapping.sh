curl -XPUT http://$ES_HOST_PORT/members/ -d '{
   "mappings":{
      "profile":{
         "properties":{
            "handleSuggest" : { 
                "type" : "completion",
                "analyzer" : "pattern",
                "search_analyzer" : "pattern",
                "payloads" : true
            },
            "skills":{
               "type":"nested",
               "properties":{
                  "score":{
                     "type":"double"
                  },
                  "sources":{"type" : "string"},
                  "name":{
                     "type":"string"
                  },
                  "id":{
                       "type":"string"
                    }
               }
            }
         }
      }
   }
}'
