# Assignment4_DAA_Zhomartov_Yerassyl

This project implements a task dependency analysis pipeline to solve scheduling problems using graph theory. It determines the **Critical Path** (Longest Path) and checks for cycle dependencies in task flows, crucial for complex urban or campus management systems.

The core problem addressed is calculating the **minimum project completion time** based on task dependencies and their individual durations (Node Durations Model).

---

 Algorithms Implemented

The analysis pipeline is built using advanced graph algorithms, all implemented with **optimal linear time complexity** $O(V+E)$:

1.  **Strongly Connected Components (SCC):** Implemented using **Tarjan's Algorithm**.
    * **Purpose:** Detects cyclic dependencies (deadlocks) in the task graph.
2.  **Condensation Graph:** A secondary graph structure built from the SCCs, where each SCC is compressed into a single node.
    * **Result:** A **Directed Acyclic Graph (DAG)**.
3.  **Topological Sort:** Implemented using **Kahn's Algorithm**.
    * **Purpose:** Provides a linear ordering of tasks (components) that respects all dependencies, essential for the Critical Path calculation.
4.  **Shortest/Longest Paths in DAG:** Implemented using **Dynamic Programming (DP)** based on the topological order.
    * **Purpose:**
        * **Longest Path:** Calculates the **Critical Path** (Minimum Project Time).
        * **Shortest Path:** Calculates the quickest possible path for any sub-sequence of tasks.

---

 Project Structure and Execution

The project uses a modular Java structure. The main logic resides in `AppMain.java`.

 Prerequisites

* Java Development Kit (JDK) 11 or higher.
* Maven (for building and running tests).

 Key Components

| File/Package | Description |
| :--- | :--- |
| `AppMain.java` | Main application entry point. Runs the full pipeline: SCC -> Condensation -> Topo Sort -> Shortest/Longest Path. |
| `data/` | Contains the **9 datasets** (`small-1..3.json`, `medium-1..3.json`, `large-1..3.json`) for testing. |
| `graph.scc.*` | Tarjan's SCC algorithm and Condensation Graph builder. |
| `graph.topo.*` | Kahn's Topological Sort algorithm. |
| `graph.dagsp.*` | Dynamic Programming for Shortest/Longest Path in a DAG. |
| `graph.util.Metrics` | Instrumentation class used to track runtime and operation counters (`dfsVisits`, `relaxations`, etc.) as required by the assignment. |


```bash
# Example: Run analysis on a medium-sized file
java AppMain data/medium-2.json
