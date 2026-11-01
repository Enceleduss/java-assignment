# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```
- First clear difference is between Product and Store entities. Product has a dedicated repository and Store is using a generic repository. I would refactor the Store entity to have a dedicated repository as well.
- Second difference is that warehouse is using domain driven approach by completely decoupling the database access strategy from the business logic. I would not do any refactors here because in such a complicated system it is necessary to have as much decoupling as possible.
```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```
Using Open API yaml for all modules might be an overkill. As some services are simply requiring basic CRUD operations and we dont neeed to have added complexity in these module. Warehouse is a fair candidate for this approach as it is very complex and has a lot of business rules and validations. I would use Open API yaml for Warehouse and keep the other modules as they are.
```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```
I believe that this part can be taken care of by AI tools very effectively. For most cost coverage we must include test cases for each and every methods in service layer. For integration tests we can use AI tools to generate test cases for each and every endpoint. We can also use AI tools to generate test cases for edge cases and negative scenarios. This will ensure that we have a good coverage of the code base and that we are testing all the possible scenarios.

```