export interface FeedSource {
  id: number;
  url: string;
  title: string;
  description: string;
  locale: string;
  lastFetchDate: Date | null;
  articles: any | null; // Adjust type as needed
}

export interface Article {
  id: number;
  title: string;
  body: string;
  link: string;
  publicationDate: string; // Consider using Date type if parsing dates
  feedSource: FeedSource;
  feedId: number | null;
}

export interface SearchResults {
  totalPages: number;
  totalElements: number;
  first: boolean;
  last: boolean;
  size: number;
  content: Article[];
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  numberOfElements: number;
  empty: boolean;
}
