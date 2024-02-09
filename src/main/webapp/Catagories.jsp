<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<%@include file="decorator/head.jsp"%>
<body class="body">
<%@include file="decorator/header.jsp"%>
<!-- page title -->
<section class="section section--first section--bg" data-bg="img/section/section.jpg">
  <div class="container">
    <div class="row">
      <div class="col-12">
        <div class="section__wrap">
          <!-- section title -->
          <h2 class="section__title">Categories</h2>
          <!-- end section title -->

          <!-- breadcrumb -->
          <ul class="breadcrumb">
            <li class="breadcrumb__item"><a href="home">Home</a></li>
            <li class="breadcrumb__item breadcrumb__item--active">Catalog list</li>
          </ul>
          <!-- end breadcrumb -->
        </div>
      </div>
    </div>
  </div>
</section>
<!-- end page title -->

<!-- filter -->
<div class="filter">
  <div class="container">
    <div class="row">
      <div class="col-12">
        <div class="filter__content">
          <div class="filter__items">
            <!-- filter item -->
            <div class="filter__item" id="filter__genre">
              <span class="filter__item-label">GENRE:</span>

              <div class="filter__item-btn dropdown-toggle" role="navigation" id="filter-genre" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <input type="button" value="Action/Adventure">
                <span></span>
              </div>

              <ul class="filter__item-menu dropdown-menu scrollbar-dropdown" aria-labelledby="filter-genre">
                <li>Action/Adventure</li>
                <li>Animals</li>
                <li>Animation</li>
                <li>Biography</li>
                <li>Comedy</li>
                <li>Cooking</li>
                <li>Dance</li>
                <li>Documentary</li>
                <li>Drama</li>
                <li>Education</li>
                <li>Entertainment</li>
                <li>Family</li>
                <li>Fantasy</li>
                <li>History</li>
                <li>Horror</li>
                <li>Independent</li>
                <li>International</li>
                <li>Kids</li>
                <li>Kids & Family</li>
                <li>Medical</li>
                <li>Military/War</li>
                <li>Music</li>
                <li>Musical</li>
                <li>Mystery/Crime</li>
                <li>Nature</li>
                <li>Paranormal</li>
                <li>Politics</li>
                <li>Racing</li>
                <li>Romance</li>
                <li>Sci-Fi/Horror</li>
                <li>Science</li>
                <li>Science Fiction</li>
                <li>Science/Nature</li>
                <li>Spanish</li>
                <li>Travel</li>
                <li>Western</li>
              </ul>
            </div>
            <!-- end filter item -->

            <!-- filter item -->
            <div class="filter__item" id="filter__quality">
              <span class="filter__item-label">QUALITY:</span>

              <div class="filter__item-btn dropdown-toggle" role="navigation" id="filter-quality" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <input type="button" value="HD 1080">
                <span></span>
              </div>

              <ul class="filter__item-menu dropdown-menu scrollbar-dropdown" aria-labelledby="filter-quality">
                <li>HD 1080</li>
                <li>HD 720</li>
                <li>DVD</li>
                <li>TS</li>
              </ul>
            </div>
            <!-- end filter item -->

            <!-- filter item -->

            <!-- end filter item -->

            <!-- filter item -->
            <div class="filter__item" id="filter__year">
              <span class="filter__item-label">RELEASE YEAR:</span>

              <div class="filter__item-btn dropdown-toggle" role="button" id="filter-year" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <div class="filter__range">
                  <div id="filter__years-start"></div>
                  <div id="filter__years-end"></div>
                </div>
                <span></span>
              </div>

              <div class="filter__item-menu filter__item-menu--range dropdown-menu" aria-labelledby="filter-year">
                <div id="filter__years"></div>
              </div>
            </div>
            <!-- end filter item -->
          </div>

          <!-- filter btn -->
          <button class="filter__btn" type="button">apply filter</button>
          <!-- end filter btn -->
        </div>
      </div>
    </div>
  </div>
</div>
<!-- end filter -->

<!-- catalog -->
<div class="catalog">
  <div class="container">
    <div class="row">
      <!-- card -->
      <c:forEach items="${films}" var="film">
      <div class="col-6 col-sm-12 col-lg-6">
        <div class="card card--list">
          <div class="row">
            <div class="col-12 col-sm-4">
              <div class="card__cover">
                <img src="${film.imageLink}" alt="${film.filmName}">
                <a href="detail?filmName=${film.filmName}" class="card__play">
                  <i class="icon ion-ios-play"></i>
                </a>
              </div>
            </div>

            <div class="col-12 col-sm-8">
              <div class="card__content">
                <h3 class="card__title"><a href="detail?filmName=${film.filmName}">${film.filmName}</a></h3>
                <span class="card__category">
										<c:forEach items="${film.categories}" var="category">
                                          <a href="#">${category.categoryName}</a>
                                        </c:forEach>
									</span>

                <div class="card__wrap">
                  <span class="card__rate">
                    <i class="icon ion-ios-star"></i>${film.ratingValue}
                     <i  style="margin-left: 10px" class="icon ion-ios-eye"></i>${film.viewCount}
                  </span>

                  <ul class="card__list">
                    <c:forEach items="${film.tags}" var="tag">
                      <li>${tag.tagName}</li>
                    </c:forEach>
                  </ul>
                </div>

                <div class="card__description">
                  <p>${film.description}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      </c:forEach>
      <!-- end card -->


      <!-- end card -->

      <!-- paginator -->
      <div class="col-12">
        <ul class="paginator paginator--list">
          <li class="paginator__item paginator__item--prev">
            <a href="#"><i class="icon ion-ios-arrow-back"></i></a>
          </li>
          <li class="paginator__item"><a href="#">1</a></li>
          <li class="paginator__item paginator__item--active"><a href="#">2</a></li>
          <li class="paginator__item"><a href="#">3</a></li>
          <li class="paginator__item"><a href="#">4</a></li>
          <li class="paginator__item paginator__item--next">
            <a href="#"><i class="icon ion-ios-arrow-forward"></i></a>
          </li>
        </ul>
      </div>
      <!-- end paginator -->
    </div>
  </div>
</div>
<!-- end catalog -->

<!-- expected premiere -->
<section class="section section--bg" data-bg="img/section/section.jpg">
  <div class="container">
    <div class="row">
      <!-- section title -->
      <div class="col-12">
        <h2 class="section__title">Expected premiere</h2>
      </div>
      <!-- end section title -->

      <!-- card -->
      <div class="col-6 col-sm-4 col-lg-3 col-xl-2">
        <div class="card">
          <div class="card__cover">
            <img src="img/covers/cover.jpg" alt="">
            <a href="#" class="card__play">
              <i class="icon ion-ios-play"></i>
            </a>
          </div>
          <div class="card__content">
            <h3 class="card__title"><a href="#">I Dream in Another Language</a></h3>
            <span class="card__category">
								<a href="#">Action</a>
								<a href="#">Triler</a>
							</span>
            <span class="card__rate"><i class="icon ion-ios-star"></i>8.4</span>
          </div>
        </div>
      </div>
      <!-- end card -->

      <!-- card -->
      <div class="col-6 col-sm-4 col-lg-3 col-xl-2">
        <div class="card">
          <div class="card__cover">
            <img src="img/covers/cover3.jpg" alt="">
            <a href="#" class="card__play">
              <i class="icon ion-ios-play"></i>
            </a>
          </div>
          <div class="card__content">
            <h3 class="card__title"><a href="#">Benched</a></h3>
            <span class="card__category">
								<a href="#">Comedy</a>
							</span>
            <span class="card__rate"><i class="icon ion-ios-star"></i>7.1</span>
          </div>
        </div>
      </div>
      <!-- end card -->

      <!-- card -->
      <div class="col-6 col-sm-4 col-lg-3 col-xl-2">
        <div class="card">
          <div class="card__cover">
            <img src="img/covers/cover2.jpg" alt="">
            <a href="#" class="card__play">
              <i class="icon ion-ios-play"></i>
            </a>
          </div>
          <div class="card__content">
            <h3 class="card__title"><a href="#">Whitney</a></h3>
            <span class="card__category">
								<a href="#">Romance</a>
								<a href="#">Drama</a>
								<a href="#">Music</a>
							</span>
            <span class="card__rate"><i class="icon ion-ios-star"></i>6.3</span>
          </div>
        </div>
      </div>
      <!-- end card -->

      <!-- card -->
      <div class="col-6 col-sm-4 col-lg-3 col-xl-2">
        <div class="card">
          <div class="card__cover">
            <img src="img/covers/cover6.jpg" alt="">
            <a href="#" class="card__play">
              <i class="icon ion-ios-play"></i>
            </a>
          </div>
          <div class="card__content">
            <h3 class="card__title"><a href="#">Blindspotting</a></h3>
            <span class="card__category">
								<a href="#">Comedy</a>
								<a href="#">Drama</a>
							</span>
            <span class="card__rate"><i class="icon ion-ios-star"></i>7.9</span>
          </div>
        </div>
      </div>
      <!-- end card -->

      <!-- card -->
      <div class="col-6 col-sm-4 col-lg-3 col-xl-2">
        <div class="card">
          <div class="card__cover">
            <img src="img/covers/cover4.jpg" alt="">
            <a href="#" class="card__play">
              <i class="icon ion-ios-play"></i>
            </a>
          </div>
          <div class="card__content">
            <h3 class="card__title"><a href="#">I Dream in Another Language</a></h3>
            <span class="card__category">
								<a href="#">Action</a>
								<a href="#">Triler</a>
							</span>
            <span class="card__rate"><i class="icon ion-ios-star"></i>8.4</span>
          </div>
        </div>
      </div>
      <!-- end card -->

      <!-- card -->
      <div class="col-6 col-sm-4 col-lg-3 col-xl-2">
        <div class="card">
          <div class="card__cover">
            <img src="img/covers/cover5.jpg" alt="">
            <a href="#" class="card__play">
              <i class="icon ion-ios-play"></i>
            </a>
          </div>
          <div class="card__content">
            <h3 class="card__title"><a href="#">Benched</a></h3>
            <span class="card__category">
								<a href="#">Comedy</a>
							</span>
            <span class="card__rate"><i class="icon ion-ios-star"></i>7.1</span>
          </div>
        </div>
      </div>
      <!-- end card -->

      <!-- section btn -->
      <div class="col-12">
        <a href="#" class="section__btn">Show more</a>
      </div>
      <!-- end section btn -->
    </div>
  </div>
</section>
<!-- end expected premiere -->

<!-- footer -->
<%@include file="decorator/footer.jsp"%>
<!-- end footer -->

<!-- JS -->
<%@include file="decorator/script.jsp"%>
</body>

</html>