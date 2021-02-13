/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.dbutils;

import org.junit.Test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import java.util.Map.Entry;
import org.apache.commons.dbutils.BaseResultSetHandler;
import java.lang.Object;
import java.lang.String;

public final class BaseResultSetHandlerTest extends BaseTestCase {

	public static BaseResultSetHandler<Collection<Map<String, Object>>> mockBaseResultSetHandler1() throws Exception {
		BaseResultSetHandler<Collection<Map<String, Object>>> mockInstance = spy(BaseResultSetHandler.class);
		doAnswer((stubInvo) -> {
			final Collection<Map<String, Object>> result = new LinkedList<>();
			while (mockInstance.next()) {
				final Map<String, Object> current = new HashMap<>();
				for (int i = 1; i <= mockInstance.getMetaData().getColumnCount(); i++) {
					current.put(mockInstance.getMetaData().getColumnName(i), mockInstance.getObject(i));
				}
				result.add(current);
			}
			return result;
		}).when(mockInstance).handle();
		return mockInstance;
	}

	@Test
	public void handleWithoutExplicitResultSetInvocation() throws Exception {
		final Collection<Map<String, Object>> result = BaseResultSetHandlerTest.mockBaseResultSetHandler1()
				.handle(createMockResultSet());

		assertFalse(result.isEmpty());

		for (final Map<String, Object> current : result) {
			assertTrue(current.containsKey("one"));
			assertTrue(current.containsKey("two"));
			assertTrue(current.containsKey("three"));
			assertTrue(current.containsKey("notInBean"));
			assertTrue(current.containsKey("intTest"));
			assertTrue(current.containsKey("integerTest"));
			assertTrue(current.containsKey("nullObjectTest"));
			assertTrue(current.containsKey("nullPrimitiveTest"));
			assertTrue(current.containsKey("notDate"));
			assertTrue(current.containsKey("columnProcessorDoubleTest"));
		}
	}

}
