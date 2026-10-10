/*
 * Copyright (c) 2025 Alireza Khodakarami
 *
 * Licensed under the MIT, (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://opensource.org/license/mit
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dynamero.ticklogic.base;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.TickStatus;
import com.dynamero.shared.properties.BEProperties;
import com.dynamero.ticklogic.node.StartNode;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Represents a logic tree used for managing tick logic in a block entity.
 *
 * @param <T> the type of block entity associated with this logic tree
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class LogicTree<T extends BlockEntity>
{
	/**
	 * The blackboard used to store and retrieve data during tick operations.
	 */
	protected Blackboard blackboard;

	/**
	 * Properties associated with the block entity.
	 */
	protected BEProperties<T> properties;

	/**
	 * The start node of the logic tree, which initiates the tick process.
	 */
	protected StartNode<T> startNode;

	/**
	 * Constructs an empty LogicTree instance.
	 */
	public LogicTree()
	{}

	/**
	 * Constructs a LogicTree instance with the specified properties, root node, blackboard, and aggregation policy.
	 *
	 * @param properties       the properties associated with the block entity
	 * @param root             the root node of the logic tree
	 * @param blackboard       the blackboard used to store data during tick operations
	 * @param aggregationPolicy the aggregation policy for combining results of individual nodes
	 */
	public LogicTree(BEProperties<T> properties, Node<T> root, Blackboard blackboard, AggregationPolicy aggregationPolicy)
	{
		this.properties = properties;
		startNode = new StartNode<>(root, aggregationPolicy);
		this.blackboard = blackboard;
		this.startNode.setTree(this);
	}

	/**
	 * Ticks the logic tree by starting the tick process from the root node.
	 *
	 * @return the tick status of the logic tree
	 */
	public TickStatus tick()
	{
		return startNode.tick();
	}

	/**
	 * Resets the logic tree, preparing it for the next tick cycle.
	 */
	public void reset()
	{
		this.startNode.reset();
	}

	/**
	 * Retrieves the blackboard used by this logic tree.
	 *
	 * @return the blackboard instance
	 */
	public Blackboard getBlackboard()
	{
		return blackboard;
	}

	/**
	 * Adds a running node to the logic tree.
	 *
	 * @param node the running node to add
	 */
	public void addRunningNode(Node<T> node)
	{
		startNode.addRunningNode(node);
	}

	/**
	 * Retrieves the block entity properties associated with this logic tree.
	 *
	 * @return the block entity properties
	 */
	public BEProperties<T> getProperties()
	{
		return this.properties;
	}
}