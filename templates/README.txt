To use these classes....

you only have to instantiate a QueryManager so look at that class.

Essentially, it stores a QueryTemplate so before you do anthing with it,
you need to load the QueryManager with a QueryTemplate, like so:

	> QueryManager q = new QueryManager();
	> q.addTemplate(new QueryTemplate("master_query.txt"));

This will use the text file "master_query.txt" as the general template
for resulting queries and will try and perform argument mapping and safety
to the parsed template.

From here, its really straighforward:

1) Examine the argument names that this query can support. To do this, you can
call

	> q.getRules();

to see the set of arguments that this query will take as input.

2) to produce a query from the QueryManager, you will get a Query object.

A Query is an immutable instance of the query you are trying to execute completed
with mapped and safety checked arguments.

It will look like this (remember q is our QueryManager instance):

	Query query = q.produceQuery(<String>arg mapping);

procuceQuery takes as an input argument a string which represents the 
argument mapping for this individual query.

here is an example (the one I tested this on)

	> QueryManager q = new QueryManager();
	> q.addTemplate(new QueryTemplate("master_query.txt"));
	> Query query = q.produceQuery("TYPEARG noodles ORIGINARG italian SIGNARG1 null PRICEARG 18.00 WORDARG *null* ORDERARG price")
	> query
and the result was:

SELECT 	d.name, r.name, r.address, serv.price, rev.rating
FROM   	Dishes as d, Restaurants as r, Serves as serv, Reviews as rev, Origin as o
WHERE  	serv.rid = r.id AND serv.did = d.id
	AND d.origin = o.origin AND d.id = rev.did
	AND d.type = noodles
 AND o.origin = italian
	AND serv.price null 18.00
ORDER BY price


HORAYYYY. Some things to note:

If you want to actually use this query in SQL, you will need to call

	> String queryToUseInSQL = query.getQuery();

and it will return a String without formatting so that it can be used.




Last thing:

QueryManager saves your argument mapping across states so if you don't want
to reuse queries or want a new one, you should call

	> q.flush();

and it will clear the argument mapping but NOT the template, so you can rerun
different argument mappings.

When providing arguments to this, you can do one of three things with arguments
that are expected by the query that are not used in your instance:

	1)	OMIT them from the input string

	2)	have <argumentName> *null* OR <argumentName> null

	3)	specify the argument name but DO NOT PROVIDE A VALUE:
			Ex: <argName1> <argName2> val2 ...