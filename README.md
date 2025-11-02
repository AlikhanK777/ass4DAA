Project Overview
This project implements advanced graph algorithms for optimizing task scheduling in smart city environments. The system processes city-service tasks (street cleaning, repairs, sensor maintenance) with complex dependencies and finds optimal execution paths using graph theory principles.

Assignment Goals
Consolidate two course topics in one practical case:
1. Strongly Connected Components (SCC) & Topological Ordering
2. Shortest Paths in Directed Acyclic Graphs (DAGs)

 Implemented Algorithms

 1. Strongly Connected Components (SCC)
- Algorithm: Tarjan's DFS-based approach
- Purpose: Detect and compress cyclic dependencies in task graphs
- Feature:
  - Finds all SCCs in directed graphs
  - Builds condensation graph (DAG of components)
  - Returns component sizes and lists

 2. Topological Sorting
- **Algorithm**: Kahn's BFS-based approach  
- **Purpose**: Linear ordering of tasks in DAG
- **Features**:
  - Valid execution order of components
  - Derived order of original tasks after SCC compression

  3. Shortest/Longest Paths in DAG
- Algorithm: Dynamic programming over topological order
- Purpose: Find optimal task execution paths
- Features:
  - Single-source shortest paths
  - Longest path finding (critical path analysis)
  - Path reconstruction for optimal routes

 Project Structure

 ass4DAA/
├── src/
│ ├── main/java/graph/
│ │ ├── Main.java # Main application entry point
│ │ ├── Metrics.java # Performance metrics collector
│ │ ├── scc/SCC.java # Tarjan's SCC implementation
│ │ ├── topo/TopologicalSort.java # Kahn's topological sort
│ │ ├── dagsp/DAGShortestPath.java # Path algorithms
│ │ └── model/GraphData.java # Data model classes
│ └── test/java/graph/
│ └── scc/SCCTest.java # JUnit tests
├── data/
│ └── tasks.json # Sample dataset
└── pom.xml # Maven configuration


Performance Metrics
The system includes comprehensive instrumentation:

SCC Metrics: DFS visits, edges traversed, execution time
Topological Sort: Queue operations (pushes/pops), timing
Path Algorithms: Relaxation steps, computation time
Memory Usage: Efficient data structures

Testing Strategy
Unit Tests

SCC on cyclic graphs and DAGs
Topological sort correctness
Path reconstruction validation
Edge case handling

Test Datasets
Small graphs (6-10 nodes): Basic functionality
Medium graphs (10-20 nodes): Mixed structures
Large graphs (20-50 nodes): Performance testing

Customization
Extending the Project

Add new graph algorithms in respective packages
Create custom dataset generators
Implement additional metrics
Extend with visualization components

Configuration
Weight models: Edge-based or node duration
Metrics granularity: Fine-grained performance tracking
Output formats: JSON, CSV, or visual reports

Student: Kenzhebek Alikhan 
