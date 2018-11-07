/*
 * Copyright (c) 2014-2018 by The Monix Project Developers.
 * See the project homepage at: https://monix.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package monix.execution.internal.collection.queues

import monix.execution.internal.collection.ConcurrentQueue
import org.jctools.queues.ConcurrentCircularArrayQueue
import scala.collection.mutable

private[internal] class FromCircularQueue[A](queue: ConcurrentCircularArrayQueue[A])
  extends ConcurrentQueue[A] {

  final def offer(elem: A): Int =
    if (queue.offer(elem)) 0 else 1
  final def poll(): A =
    queue.poll()
  final def clear(): Unit =
    queue.clear()

  final def drainToBuffer(buffer: mutable.Buffer[A], limit: Int): Int = {
    val consume = new QueueDrain[A](buffer)
    queue.drain(consume, limit)
    consume.count
  }
}