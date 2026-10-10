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

package com.dynamero.gui.client.constants;

import com.dynamero.gui.client.records.TextureData;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class TabTextures
{
	public static class Top
	{
		public static final TextureData NORMAL =
				new TextureData(0, -20, 0, 0, 23, 20, 32, 32,
                                BaseHelper.id("jilibs_gui", "textures/gui/container/tab_top_normal.png"));
		public static final TextureData SELECTED =
				new TextureData(0, -25, 0, 0, 23, 27, 32, 32,
								BaseHelper.id("jilibs_gui", "textures/gui/container/tab_top_selected.png"));
	}

	public static class Bottom
	{
		public static final TextureData NORMAL =
				new TextureData(0, 0, 0, 0, 23, 20, 32, 32,
								BaseHelper.id("jilibs_gui", "textures/gui/container/tab_bottom_normal.png"));
		public static final TextureData SELECTED =
				new TextureData(0, -2, 0, 0, 23, 27, 32, 32,
								BaseHelper.id("jilibs_gui", "textures/gui/container/tab_bottom_selected.png"));
	}

	public static class Left
	{
		public static final TextureData NORMAL =
				new TextureData(-20, 0, 0, 0, 20, 23, 32, 32,
								BaseHelper.id("jilibs_gui", "textures/gui/container/tab_left_normal.png"));
		public static final TextureData SELECTED =
				new TextureData(-25, 0, 0, 0, 27, 23, 32, 32,
								BaseHelper.id("jilibs_gui", "textures/gui/container/tab_left_selected.png"));
	}

	public static class Right
	{
		public static final TextureData NORMAL =
				new TextureData(0, 0, 0, 0, 20, 23, 32, 32,
								BaseHelper.id("jilibs_gui", "textures/gui/container/tab_right_normal.png"));
		public static final TextureData SELECTED =
				new TextureData(-2, 0, 0, 0, 27, 23, 32, 32,
								BaseHelper.id("jilibs_gui", "textures/gui/container/tab_right_selected.png"));
	}
}