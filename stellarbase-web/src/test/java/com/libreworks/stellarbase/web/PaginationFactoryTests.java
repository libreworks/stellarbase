package com.libreworks.stellarbase.web;

import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.common.collect.ImmutableMap;
import com.libreworks.stellarbase.persistence.Pagination;

public class PaginationFactoryTests
{
	@Test
	public void testDojoStore()
	{
		PaginationFactory object = new PaginationFactory();
		
		MockHttpServletRequest request1 = MockMvcRequestBuilders.request(HttpMethod.GET, URI.create("foo"))
			.header("Range", "items=1-25")
			.param("sort(+foo,-bar)", "")
			.buildRequest(new MockServletContext());
		Pagination p1 = object.create(request1);
		
		assertEquals(Integer.valueOf(25), p1.getMax());
		assertEquals(Integer.valueOf(1), p1.getOffset());
		assertEquals(ImmutableMap.of("foo", Boolean.TRUE, "bar", Boolean.FALSE), p1.getOrder());
		
		MockHttpServletRequest request2 = MockMvcRequestBuilders.request(HttpMethod.GET, URI.create("foo"))
			.param("sort(+foo)", "")
			.buildRequest(new MockServletContext());
		Pagination p2 = object.create(request2);
		
		assertEquals(Integer.valueOf(Integer.MAX_VALUE), p2.getMax());
		assertEquals(Integer.valueOf(0), p2.getOffset());
		assertEquals(ImmutableMap.of("foo", Boolean.TRUE), p2.getOrder());
		
		MockHttpServletRequest request3 = MockMvcRequestBuilders.request(HttpMethod.GET, URI.create("foo"))
			.header("Range", "items=0-")
			.param("sortBy", "+foo,-bar,baz")
			.buildRequest(new MockServletContext());
		Pagination p3 = object.create(request3, "sortBy");
		
		assertEquals(Integer.valueOf(Integer.MAX_VALUE), p3.getMax());
		assertEquals(Integer.valueOf(0), p3.getOffset());
		assertEquals(ImmutableMap.of("foo", Boolean.TRUE, "bar", Boolean.FALSE, "baz", Boolean.TRUE), p3.getOrder());		
	}
	
	@Test
	public void testDojoData()
	{
		PaginationFactory object = new PaginationFactory();
		
		MockHttpServletRequest request1 = MockMvcRequestBuilders.request(HttpMethod.GET, URI.create("foo"))
			.param("count", "25")
			.param("start", "1")
			.param("sort", "+foo,-bar")
			.buildRequest(new MockServletContext());
		Pagination p1 = object.create(request1);
		
		assertEquals(Integer.valueOf(25), p1.getMax());
		assertEquals(Integer.valueOf(1), p1.getOffset());
		assertEquals(ImmutableMap.of("foo", Boolean.TRUE, "bar", Boolean.FALSE), p1.getOrder());
	}
	
	@Test
	public void testExtJs(){
		PaginationFactory object = new PaginationFactory();
		
		MockHttpServletRequest request1 = MockMvcRequestBuilders.request(HttpMethod.GET, URI.create("foo"))
			.param("start", "1")
			.param("limit", "25")
			.param("sort", "[{\"property\":\"foo\",\"direction\":\"asc\"},{\"property\":\"bar\",\"direction\":\"desc\"}]")
			.buildRequest(new MockServletContext());
		Pagination p1 = object.create(request1);
		
		assertEquals(Integer.valueOf(25), p1.getMax());
		assertEquals(Integer.valueOf(1), p1.getOffset());
		assertEquals(ImmutableMap.of("foo", Boolean.TRUE, "bar", Boolean.FALSE), p1.getOrder());
	}
	
	@Test
	public void testOpenSearchServer()
	{
		PaginationFactory object = new PaginationFactory();
		
		MockHttpServletRequest request1 = MockMvcRequestBuilders.request(HttpMethod.GET, URI.create("foo"))
			.param("startIndex", "2")
			.param("count", "25")
			.param("sort", "foo", "-bar")
			.buildRequest(new MockServletContext());
		Pagination p1 = object.create(request1);
		
		assertEquals(Integer.valueOf(25), p1.getMax());
		assertEquals(Integer.valueOf(1), p1.getOffset());
		assertEquals(ImmutableMap.of("foo", Boolean.TRUE, "bar", Boolean.FALSE), p1.getOrder());
		
		MockHttpServletRequest request2 = MockMvcRequestBuilders.request(HttpMethod.GET, URI.create("foo"))
			.param("startPage", "2")
			.param("count", "25")
			.param("sort", "foo", "-bar")
			.buildRequest(new MockServletContext());
		Pagination p2 = object.create(request2);
		
		assertEquals(Integer.valueOf(25), p2.getMax());
		assertEquals(Integer.valueOf(25), p2.getOffset());
		assertEquals(ImmutableMap.of("foo", Boolean.TRUE, "bar", Boolean.FALSE), p2.getOrder());
	}
	
	@Test
	public void testOpenSearchExtension()
	{
		PaginationFactory object = new PaginationFactory();
		
		MockHttpServletRequest request1 = MockMvcRequestBuilders.request(HttpMethod.GET, URI.create("foo"))
			.param("startIndex", "2")
			.param("count", "25")
			.param("sort", "foo:ascending", "bar:descending")
			.buildRequest(new MockServletContext());
		Pagination p1 = object.create(request1);
		
		assertEquals(Integer.valueOf(25), p1.getMax());
		assertEquals(Integer.valueOf(1), p1.getOffset());
		assertEquals(ImmutableMap.of("foo", Boolean.TRUE, "bar", Boolean.FALSE), p1.getOrder());
		
		MockHttpServletRequest request2 = MockMvcRequestBuilders.request(HttpMethod.GET, URI.create("foo"))
			.param("startPage", "2")
			.param("count", "25")
			.param("sort", "foo:ascending", "bar:descending")
			.buildRequest(new MockServletContext());
		Pagination p2 = object.create(request2);
		
		assertEquals(Integer.valueOf(25), p2.getMax());
		assertEquals(Integer.valueOf(25), p2.getOffset());
		assertEquals(ImmutableMap.of("foo", Boolean.TRUE, "bar", Boolean.FALSE), p2.getOrder());
	}
	
	@Test
	public void testSpringData()
	{
		PaginationFactory object = new PaginationFactory();
		
		MockHttpServletRequest request2 = MockMvcRequestBuilders.request(HttpMethod.GET, URI.create("foo"))
			.param("page", "2")
			.param("limit", "25")
			.param("sort", "foo,asc", "bar,desc")
			.buildRequest(new MockServletContext());
		Pagination p2 = object.create(request2);
		
		assertEquals(Integer.valueOf(25), p2.getMax());
		assertEquals(Integer.valueOf(25), p2.getOffset());
		assertEquals(ImmutableMap.of("foo", Boolean.TRUE, "bar", Boolean.FALSE), p2.getOrder());
	}
	
	@Test
	public void testGrails()
	{
		PaginationFactory object = new PaginationFactory();
		
		MockHttpServletRequest request1 = MockMvcRequestBuilders.request(HttpMethod.GET, URI.create("foo"))
			.param("max", "25")
			.param("offset", "1")
			.param("sort", "foo")
			.param("order", "asc")
			.buildRequest(new MockServletContext());
		Pagination p1 = object.create(request1);
		
		assertEquals(Integer.valueOf(25), p1.getMax());
		assertEquals(Integer.valueOf(1), p1.getOffset());
		assertEquals(ImmutableMap.of("foo", Boolean.TRUE), p1.getOrder());
	}
}
