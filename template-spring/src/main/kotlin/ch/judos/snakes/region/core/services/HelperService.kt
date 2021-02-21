package ch.judos.snakes.region.core.services

import org.springframework.stereotype.Service


@Service
class HelperService {


	fun getSortOrder(sortParam: String?): Pair<String?, Boolean> {
		var desc = false
		var sort = sortParam
		if (sort?.endsWith(",desc") == true) {
			desc = true
			sort = sort.substring(0, sort.length - 5)
		}
		if (sort?.endsWith(",asc") == true) {
			desc = false
			sort = sort.substring(0, sort.length - 4)
		}
		return Pair(sort, desc)
	}

}
