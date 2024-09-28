import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SearchResults, Article } from '../model';

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  private apiUrl = 'http://localhost:8080/articles/search'; // Your API URL

  constructor(private http: HttpClient) {}

  search(query: string, page: number = 0, size: number = 20, sort: string = 'publication_date,desc'): Observable<SearchResults> {
    // Construct query parameters
    let params = {
      query,
      page: page.toString(),
      size: size.toString(),
      sort,
    }

    // Perform the HTTP GET request with parameters
    return this.http.get<SearchResults>(this.apiUrl, { params });
  }
}

