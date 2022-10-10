#!/bin/bash

nr=${1:-5}

asadmin --port=${nr}4848 add-library --upload=true ${0%/*}/postgresql-42.2.6.jar

