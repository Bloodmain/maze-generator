# Maze generators and solvers

The library contains maze generators based on Kruskal's and Recursive backtracking algorithms.
In addition, it has solvers based on Dijkstra's and Ford-Bellman's algorithms. 
The library can print generated maze and the found path.
The generated mazes cells may have different surfaces (the efforts to pass the cell) which is taken into account by solvers.

## Some features:
 - The generators are configurable: portion of cycles, bad/good surfaces, entry and exit points of the maze
 - The solvers are configurable: cost of different types of surfaces
 - The renderer prints the colorized maze
 - The solvers also counts the statistic (cost of all path, the number of passed surfaces)
