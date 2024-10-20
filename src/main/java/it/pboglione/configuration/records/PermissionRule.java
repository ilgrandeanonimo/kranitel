/*
 *
 *  PermissionRule.java
 *  This file is part of Kranitel by IlGrandeAnonimo
 *  Copyright (C) 2024 IlGrandeAnonimo <ilgrandeanonimo@icloud.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.pboglione.configuration.records;

import org.bukkit.permissions.PermissionDefault;

public record PermissionRule(PermissionDefault isDefault, String description) {
}
