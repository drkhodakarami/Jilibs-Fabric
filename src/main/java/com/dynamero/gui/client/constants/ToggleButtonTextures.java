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
@CreatedAt("2026-08-10")
@ModifiedAt("2026-08-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class ToggleButtonTextures
{
	public static class Normal
	{
		public static class Off
		{
			public static final TextureData NORMAL =
					new TextureData(0, 0, 0, 0, 32, 16, 32, 16,
                                    BaseHelper.id("jilibs_gui", "textures/gui/widget/toggle_switch_off.png"));
			public static final TextureData HOVERED =
					new TextureData(0, 0, 0, 0, 32, 16, 32, 16,
									BaseHelper.id("jilibs_gui", "textures/gui/widget/selected_toggle_switch_off.png"));
		}

		public static class On
		{
			public static final TextureData NORMAL =
					new TextureData(0, 0, 0, 0, 32, 16, 32, 16,
									BaseHelper.id("jilibs_gui", "textures/gui/widget/toggle_switch_on.png"));
			public static final TextureData HOVERED =
					new TextureData(0, 0, 0, 0, 32, 16, 32, 16,
									BaseHelper.id("jilibs_gui", "textures/gui/widget/selected_toggle_switch_on.png"));
		}
	}

	public static class Colored
	{
		public static class Off
		{
			public static final TextureData NORMAL =
					new TextureData(0, 0, 0, 0, 32, 16, 32, 16,
									BaseHelper.id("jilibs_gui", "textures/gui/widget/toggle_switch_green_on.png"));
			public static final TextureData HOVERED =
					new TextureData(0, 0, 0, 0, 32, 16, 32, 16,
									BaseHelper.id("jilibs_gui", "textures/gui/widget/selected_toggle_switch_green_on.png"));
		}

		public static class On
		{
			public static final TextureData NORMAL =
					new TextureData(0, 0, 0, 0, 32, 16, 32, 16,
									BaseHelper.id("jilibs_gui", "textures/gui/widget/toggle_switch_red_off.png"));
			public static final TextureData HOVERED =
					new TextureData(0, 0, 0, 0, 32, 16, 32, 16,
									BaseHelper.id("jilibs_gui", "textures/gui/widget/selected_toggle_switch_red_off.png"));
		}
	}

	public static class Small
	{
		public static class Off
		{
			public static final TextureData NORMAL =
					new TextureData(0, 0, 0, 0, 19, 10, 32, 16,
									BaseHelper.id("jilibs_gui", "textures/gui/widget/toggle_button_green_on.png"));
			public static final TextureData HOVERED =
					new TextureData(0, 0, 0, 0, 19, 10, 32, 16,
									BaseHelper.id("jilibs_gui", "textures/gui/widget/selected_toggle_button_green_on.png"));
		}

		public static class On
		{
			public static final TextureData NORMAL =
					new TextureData(0, 0, 0, 0, 19, 10, 32, 16,
									BaseHelper.id("jilibs_gui", "textures/gui/widget/toggle_button_red_off.png"));
			public static final TextureData HOVERED =
					new TextureData(0, 0, 0, 0, 19, 10, 32, 16,
									BaseHelper.id("jilibs_gui", "textures/gui/widget/selected_toggle_button_red_off.png"));
		}
	}
}