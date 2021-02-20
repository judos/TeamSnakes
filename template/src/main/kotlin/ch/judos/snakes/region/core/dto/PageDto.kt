package ch.judos.snakes.region.core.dto

import org.springframework.data.domain.Page

class PageDto<T>(page: Page<T>) {

	val content: List<T> = page.content
	val pageIndex: Int = page.pageable.pageNumber
	val pageSize: Int = page.pageable.pageSize
	val totalPages: Int = page.totalPages
	val totalElements: Long = page.totalElements

}
