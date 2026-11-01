# Case Study Scenarios to discuss

## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.

**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations

**Questions you may have and considerations:**
[ Tracking cost across warehouses and stores may get super complicated. Some questions to consider: How do we define the cost? It can be warehouse specific/ store specific/ orders specific/ product specific. And moreover costing of a single order may span across multiple warehouses and store. To clearly solve this problem we will have to think of a fully decoupled cost management module in the system that clearly identifies the sources of the cost eg; it can be item cost/staff salary/rent/logistics and many more. Since the requirement is to track costs warehouse wise and store wise, we will have to think of a cost allocation strategy that can be applied to the cost management module. Eg; if we have a warehouse that is fulfilling orders for multiple stores, how do we allocate the cost of that warehouse to the different stores? We can think of a cost allocation strategy based on the number of orders fulfilled for each store or based on the revenue generated from each] store. ]

## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.

**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?

**Questions you may have and considerations:**
[ Cost optimization can be done by smartly allocating correct warehouses to correct stores as fulfilment units. Considering geographical lcation/ physical distance from the store/ Electricity rates/ Government statutory charges/ Labor costs/ Transportation costs etc. We can also think of following standard cost management strategies followed by the industry ]

## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.

**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have from that and how would you ensure seamless integration and data synchronization?

**Questions you may have and considerations:**
[ Since cost tracking is not a static process, it is important to have a real-time integration with the financial systems. This will ensure that the cost data is always up-to-date and accurate. Some questions to consider: What financial systems are currently in use? What data needs to be synchronized? How often should the synchronization occur? What are the potential challenges in integrating with these systems? How can we ensure data integrity and consistency across systems? ]

## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.

**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?

**Questions you may have and considerations:**
[ Since fulfilment operations are very dynamic depending on consumer demands/supply chain disruptions/seasonal variations etc, it is important to have a robust budgeting and forecasting system in place. To support accurate budgeting, we need to carefully observe the current cost per warehouse/store and see how much revenue is being generated from respective entities. Forcasting can be done correctly if we have a good forcasting for consumer demands/weather condition forcasting as logistics get impacted/how likely is that a product will get damaged dring transportation etc. ]

## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.

**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?

**Questions you may have and considerations:**
[ It is but obvious that preserving previous cost history of archived warehouse is very important for future analytics. Since many parameters remain same for the new warehouse, like geographical location/Labour cost/Weather conditions etc. We can do cost optimization by replacing a warehouse by increasing/decreasing its capacity or by setting it up in an area where road infrastructure and connectivity is better. ]

## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.

**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.
