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

package com.dynamero.ticklogic.node;

import java.util.ArrayList;
import java.util.List;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.enumerations.TickStatus;
import com.dynamero.ticklogic.base.AggregationPolicy;
import com.dynamero.ticklogic.base.IRunningNodeManager;
import com.dynamero.ticklogic.base.Node;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Represents the start node in a tick logic system.
 *
 * <p>The StartNode manages the execution of a tree of nodes, handling running nodes and aggregating their results based on the specified aggregation policy.</p>
 */
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class StartNode<T extends BlockEntity> extends Node<T>
{
	/**
	 * A list of currently running child nodes.
	 */
	private List<Node<T>> runningNodes = new ArrayList<>();

	/**
	 * The aggregation policy to determine the final tick status based on child node results.
	 */
	private final AggregationPolicy aggregationPolicy;

	/**
	 * Constructs a StartNode with no Blackboard and a specified aggregation policy.
	 *
	 * @param root the root node of the tick logic tree
	 * @param aggregationPolicy the aggregation policy to determine the final tick status
	 */
	public StartNode(Node<T> root, AggregationPolicy aggregationPolicy)
	{
		super();
		children.add(root);
		this.aggregationPolicy = aggregationPolicy;
	}

	/**
	 * Evaluates the child nodes based on the specified aggregation policy and returns a tick status.
	 *
	 * @return the final tick status based on the evaluated child nodes
	 */
	@Override
	public TickStatus tick()
	{
		if(children.isEmpty())
			return TickStatus.ERROR;

		if(children.size() > 1)
			return TickStatus.ERROR;

		if(runningNodes.isEmpty())
		{
			this.tree.reset();

			TickStatus result = children.getFirst().tick();

			if(!(children.getFirst().getParent() instanceof IRunningNodeManager) && result == TickStatus.RUNNING)
				runningNodes.add(children.getFirst());

			return result;
		}
		else
		{
			List<Node<T>> nextRunningNodes = new ArrayList<>();

			int successCount = 0, failedCount = 0, runningCount = 0;

			for (Node<T> node : runningNodes)
			{
				TickStatus result = node.tick();

				switch (result)
				{
					case TickStatus.SUCCESS: successCount++; break;
					case TickStatus.FAILURE: failedCount++; break;
					case TickStatus.RUNNING:
						if (!(node.getParent() instanceof IRunningNodeManager))
							nextRunningNodes.add(node);
						runningCount++;
						break;
				}
			}

			runningNodes = nextRunningNodes;

			if(runningCount > 0) return TickStatus.RUNNING;

			//noinspection EnhancedSwitchMigration
			switch (aggregationPolicy)
			{
				case SUCCEED_ON_ANY_SUCCESS, FAIL_IF_ALL_FAILURE:
					if(successCount > 0) return TickStatus.SUCCESS;
					return TickStatus.FAILURE;
				case FAIL_ON_ANY_FAILURE, SUCCEED_IF_ALL_SUCCESS:
					if(failedCount > 0) return TickStatus.FAILURE;
					return TickStatus.SUCCESS;
				default:
					return TickStatus.FAILURE;
			}
		}
	}

	/**
	 * Adds a running node to the list of currently running nodes.
	 *
	 * @param node the node to add as running
	 */
	public void addRunningNode(Node<T> node)
	{
		if(node.getParent() instanceof IRunningNodeManager)
			return;

		if(!runningNodes.contains(node))
			runningNodes.add(node);
	}

	/**
	 * Resets the state of this node and clears the list of running nodes.
	 */
	@Override
	public void reset()
	{
		super.reset();
		runningNodes.clear();
	}
}