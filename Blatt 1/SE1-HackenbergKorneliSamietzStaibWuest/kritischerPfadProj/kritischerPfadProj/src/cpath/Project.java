package cpath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

/*
 * @author Franz Hackenberg, 		MN:3338863, st155960@stud.uni-stuttgart.de
 * @author Clemens Korneli, 		MN:3538867, st172943@stud.uni-stuttgart.de
 * @author Jannick Leif Samietz, 	MN:3333295, st158516@stud.uni-stuttgart.de
 * @author Christian Staib, 		MN:3391431, st162081@stud.uni-stuttgart.de
 * @author Florian Wuest, 			MN:3528877, st171957@stud.uni-stuttgart.de
 */

/**
 * Stellt ein Projekt dar, das aus Arbeitspaketen besteht. Im Prinzip nichts
 * anderes als ein Graph mit Arbeitspaketen als Knoten. Das Projekt kennt nur
 * die Startknoten, diese kennen jeweils ihren Nachfolger.
 */
public class Project {

	private List<Workpackage> startNodes = new ArrayList<Workpackage>();
	public List<Workpackage> endNodes = new ArrayList<Workpackage>();
	private Set<Workpackage> criticalPathNodes = new HashSet<Workpackage>();
	private Set<Workpackage> allNodes = new HashSet<Workpackage>();
	private Stack<Workpackage> currentNodes = new Stack<Workpackage>();

	private int maxProjectDuration = 0;

	public List<Workpackage> getStartNodes() {
		return startNodes;
	}

	public void setStartNodes(List<Workpackage> startNodes) {
		this.startNodes = startNodes;
	}

	public Set<Workpackage> getCriticalPathNodes() {
		return criticalPathNodes;
	}

	public Set<Workpackage> getAllNodes() {
		return allNodes;
	}

	/**
	 * fills criticalNodes with all Workpackages that represent the critical path
	 */
	public void computeCriticalPath() {
		resetLists();
			
		computeEarlyStartAndEarlyFinish();
		computeLateStartAndLateFinish();
		computeSlack();
		computeCriticalNodes();
	}

	/**
	 * Adds all nodes in this.allNodes with a slack of 0 to critical Nodes
	 *
	 * ! slack must be filled for all nodes
	 */
	private void computeCriticalNodes() {
		this.allNodes.forEach(node -> {
			if(node.getSlack() == 0){
				criticalPathNodes.add(node);
			}
		});
	}

	/**
	 * calculates every node's slack by latest. minus the earliest start
	 *
	 * ! earlyStart and latestStart must be filled for all nodes
	 */
	private void computeSlack() {
		this.allNodes.parallelStream().forEach(node -> {
			node.setSlack(node.getLatestStart() - node.getEarliestStart());
		});
	}

	/**
	 * fills latestStart and latestFinish with the correct values
	 *
	 * ! relies on this. endNodes, maxProjectDuration and early- Start and Finish of every node
	 */
	private void computeLateStartAndLateFinish() {
		computeLatestEndValues();
		while(!currentNodes.isEmpty()) {
			Workpackage node = currentNodes.pop();
			node.setLatestFinish(computeLatestFinish(node));
			node.setLatestStart(node.getLatestFinish() - node.getDuration());
			currentNodes.addAll(node.getPredecessors());
		}
	}

	/**
	 * computes the LatestFinish for a given node
	 *
	 * ! its successors must have a latestStart
	 *
	 * @param node for who latestFinish is filled
	 * @return calculated latestFinish
	 */
	private int computeLatestFinish(final Workpackage node) {
		if(node == null) {
			throw new IllegalArgumentException();
		}
		int result = Integer.MAX_VALUE;
		for (Workpackage successor: node.getSuccessors()) {
			if(successor.getLatestStart() == Integer.MAX_VALUE){
				return Integer.MAX_VALUE;
			}
			if(successor.getLatestStart() < result) {
				result = successor.getLatestStart();
			}
		}
		return result;
	}

	/**
	 * Fills the LatestFinish and LatestStart values for all nodes in endNodes
	 *
	 * ! maxProjectDuration must be correct
	 */
	private void computeLatestEndValues() {
		this.endNodes.forEach(endNode -> {
			endNode.setLatestFinish(this.maxProjectDuration);
			endNode.setLatestStart(endNode.getLatestFinish() - endNode.getDuration());
			endNode.getPredecessors().forEach(currentNodes::push);
		});
	}

	/**
	 * Goes through the tree in level order and fills the earlyStart and earlyFinish values
	 *
	 * ! tree must be fully initialized
	 */
	private void computeEarlyStartAndEarlyFinish() {
		computeStartValues();
		while(!currentNodes.empty()){
			Workpackage node = currentNodes.pop();
			node.setEarliestStart(computeEarlyStart(node));
			node.setEarliestFinish(node.getEarliestStart() + node.getDuration());
			checkEndNode(node);
		}
	}

	/**
	 * looks at all predecessors of a given node and returns the highest early start
	 *
	 * @param node given Workpackage for whom earlyStart is calculated
	 * @return highest earlyStart of predecessors
	 */
	private int computeEarlyStart(final Workpackage node) {
		if(node == null) {
			throw new IllegalArgumentException();
		}
		int result = 0;
		for (Workpackage predecessor: node.getPredecessors()) {
			if(predecessor.getEarliestFinish() > result){
				result = predecessor.getEarliestFinish();
			}
		}
		return result;
	}

	/**
	 * Fills earlyStart and earlyFinish for all nodes in startNodes,
	 * then adds them to currentNodes for processing
	 *
	 * ! startNodes must not be empty
	 */
	private void computeStartValues() {
		this.startNodes.forEach(startNode -> {
			startNode.setEarliestStart(0);
			startNode.setEarliestFinish(startNode.getDuration());
			checkEndNode(startNode);
		});
	}

	/**
	 * preforms a check on a given node if it has no successors,
	 * if so, it is added to endNodes and maxProjectDuration is updated
	 * if not, all successors are added to currentNodes
	 *
	 * @param potentialEndNode given potential end Node
	 */
	private void checkEndNode(final Workpackage potentialEndNode) {
		if (potentialEndNode == null){
			throw new IllegalArgumentException();
		}
		allNodes.add(potentialEndNode);
		if(potentialEndNode.getSuccessors().size() == 0){
				endNodes.add(potentialEndNode);
				updateMaxProjectDuration(potentialEndNode);
		}else {
			potentialEndNode.getSuccessors().forEach(currentNodes::push);
		}
	}

	/**
	 * checks if a given endNode has an earlyFinish greater than the maxProjectDuration,
	 * if so, maxProjectDuration is updated
	 *
	 * @param endNode given Node that has no successors that is checked
	 */
	private void updateMaxProjectDuration(final Workpackage endNode) {
		if (endNode == null) {
			throw new IllegalArgumentException();
		}
		if(endNode.getEarliestFinish() > this.maxProjectDuration){
			this.maxProjectDuration = endNode.getEarliestFinish();
		}
	}


	private void resetLists() {
		currentNodes.clear();
		endNodes.clear();
		criticalPathNodes.clear();
		allNodes.clear();
	}
}
