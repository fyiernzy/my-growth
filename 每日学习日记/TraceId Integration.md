---
updated: 2025-07-28T11:20:44.389+08:00
edited_seconds: 162
---
# Phase 1: 

- [ ] Define a way to customize traceId generatio and propose the possible ways
- [ ] Specify the format to be reviewed for the logback (traceId, className, methodName)
- [ ] Include the format for both log & sql log
- [ ] Define an AOP to intercept the outgoing responses with the traceId.

# Phase 2:

- [ ] Grafana + Tempo + Loki Integration with Docker
	- [ ] Configuring TraceId to search
	- [ ] Explore this technical stack capabilities and how it can be used to aid development/ debugging processes
	- [ ] Warning mechanism
- [ ] Execution time visualization & Display (For future load tests)