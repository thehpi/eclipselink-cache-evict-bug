# Payara Issue

This repo was created as a reproducer for [this](https://github.com/eclipse-ee4j/eclipselink/issues/?) ticket.

# Description

I have a parent entity which is @Cacheable which has two @OneToMany child entities and one of the two is @Cacheable and the other is not.

Now I update, using sql, a record of the @Cacheble child entity.

When I now retrieve the parent entity I don't see the updated child. This is correct.

Now I evict the child record using the entity manager factory.

When I now retrieve the parent entity again I would expect the child record to show the changed value, but it does not.

Only after also evicting the parent entity, and getting it again I see the updated child record.

Interesting detail, if I turn on logging, and I retrieve the parent entity (after evicting the child record) I do see a query being executed for the evicted child, but for some reason the results are not stored, or at least not visible when retrieving.

I created a reproducer so you can check it out:

## Steps to reproduce

To build the environment run

	./build.sh

This will

	- Start the docker instances with payara server and the postgresql database.
	- Wait 20 seconds for payara to start
	- Setup paraya configuration
	- Build the software
	- Deploy the software

Now create some data

	- Create the parent entity object
		- ./test-create.sh
			- this will return something like
				{
				    "children": [],
				    "friends": [],
				    "id": "0cd166f2-fed8-47cb-9930-e5af8d1ffdc3",
				    "test": "test1"
				}
		- Note down the id of this object
		- export id=<the id you created>
	- Create a child entity object
		- ./test-create-child.sh "${id}"
			- This will show something like
				{
				    "id": "5e56c7bf-769d-4da7-a793-49a965815862",
				    "test": "test1"
				}
		- Note down the child id of this object
		- export childId=<the child id you created>

Now run the script that shows the problem (this expects 'id' and 'childId' to be set)

	- ./test.sh

This script should show something like this:

	Testing simple app
	Get Hans
	{
	  "id": "32f8b826-1ad0-4102-b4f5-839d7189e657",
	  "test": "test1"
	}
	Update 'test' to 'test2'
	Get Hans, test should be the same
	{
	  "id": "32f8b826-1ad0-4102-b4f5-839d7189e657",
	  "test": "test1"
	}
	Evict child: true
	Get Hans, test should be changed
	{
	  "id": "32f8b826-1ad0-4102-b4f5-839d7189e657",
	  "test": "test1"
	}
	This is the actual problem: expected different values
	v1:test1
	v2:test1
	Evict hans: true
	Get Hans, now test should be changed
	{
	  "id": "32f8b826-1ad0-4102-b4f5-839d7189e657",
	  "test": "test2"
	}
	INDEED, now it works (but the problem still exists)!

If you don't see the last line with INDEED here then for some reason this problem does not exist in your environment. 
Maybe the bug is fixed?

If you do see this then continue

	- Go to the source file 
		- HansFriend.java
	- and remove line 8
		- @Cacheable(false)
	- Rebuild the project
		- mvn clean install
	- ./redeploy.sh
    - Rerun the test
    - ./test.sh
    - Now you will see something like this
        Get Hans
        {
        "id": "32f8b826-1ad0-4102-b4f5-839d7189e657",
        "test": "test2"
        }
        Update 'test' to 'test1'
        Get Hans, 'test' should be the same
        {
        "id": "32f8b826-1ad0-4102-b4f5-839d7189e657",
        "test": "test2"
        }
        Evict child: true
        Get Hans, 'test' should be changed
        {
        "id": "32f8b826-1ad0-4102-b4f5-839d7189e657",
        "test": "test1"
        }
        YAAY, it works! Is the problem solved?
    - YAAY means there is no problem anymore so making both child entities @Cacheable(true) fixed the problem

## Environment ##

- **Payara Version**: 5.2022.2
- **Edition**: Server
- **Eclipselink version**: 2.7.9.payara-p1
- **JDK Version**: openjdk 11.0.15
- **Operating System**: Mac
- **Database**: PostgreSQL 13.5
