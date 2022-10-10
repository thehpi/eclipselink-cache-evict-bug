curl -s -H "Accept: application/json" "http://localhost:58080/${application}/resources/test/hans/${id}" | jq '.children[] | select( .id == "'${childId}'")'
