import React from "react";

const SearchInput = ({
  onSearchClick,
  onInputChange,
  queryString,
  disabled,
}) => {
  return (
    <form
      className="usa-search usa-search--small prime-search-input"
      role="search"
    >
      <label className="usa-sr-only" htmlFor="search-field-small">
        Search
      </label>
      <input
        autoComplete="off"
        className="usa-input"
        id="search-field-small"
        type="search"
        name="search"
        value={queryString}
        onChange={onInputChange}
      />
      <button
        className="usa-button"
        type="submit"
        disabled={disabled}
        onClick={onSearchClick}
      >
        <span className="usa-sr-only">Search</span>
      </button>
    </form>
  );
};

export default SearchInput;
