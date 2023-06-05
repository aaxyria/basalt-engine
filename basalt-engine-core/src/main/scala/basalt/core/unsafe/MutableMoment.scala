/**
 * Basalt Engine, a server-side modding engine for Minecraft third-party server implementations
 * Copyright (C) 2023 Pedro Henrique
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package basalt.core.unsafe

import basalt.core.datatype.Tick
import basalt.unsafe.{UnsafeStructKind, Pointer}

class MutableMoment(val address: Pointer)(using kind: UnsafeStructKind[Int]) {
  def addedAt_=(tick: Tick): Pointer =
    kind.store(address, 0, tick.toInt)
  def addedAt: Tick =
    Tick(kind.read(Pointer.buildAddress(address, 0, kind.alignment)))
  def updatedAt_=(tick: Tick): Pointer =
    kind.store(address, 1, tick.toInt)
  def updatedAt: Tick =
    Tick(kind.read(Pointer.buildAddress(address, 1, kind.alignment)))
  def removedAt_=(tick: Tick): Pointer =
    kind.store(address, 2, tick.toInt)
  def removedAt: Option[Tick] =
    kind.read(Pointer.buildAddress(address, 2, kind.alignment)) match {
      case 0    => None
      case tick => Some(Tick(tick))
    }
}

object MutableMoment {
  def apply(using kind: UnsafeStructKind[Int]): MutableMoment =
    new MutableMoment(kind.allocEmpty(4))
}
