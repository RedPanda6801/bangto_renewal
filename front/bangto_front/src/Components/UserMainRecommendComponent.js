import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { resContent } from "./UtilComponent/ResponseData";

const UserMainRecommendComponent = () => {
  const [currentSlide, setCurrentSlide] = useState(0); 
  const [currentWishlist, setCurrentWishlist] = useState(0);
  const [totalWishlistItems, setTotalWishlistItems] = useState(0);
  const [wishlistItems, setWishlistItems] = useState([]);
  const [recommendItems, setRecommendItems] = useState([]);
  const navigate = useNavigate();
  
  const slides = ["images/03_main/main_01.jpg",
    "images/03_main/main_02.jpg",
    "images/03_main/main_03.jpg"];
  const totalSlides = slides.length;
  const itemsPerPage = 5;

  const moveToNextSlide = () => { setCurrentSlide((prev) => (prev + 1) % totalSlides);                };
  const moveToPrevSlide = () => { setCurrentSlide((prev) => (prev - 1 + totalSlides) % totalSlides);  };

  useEffect(() => 
  {
    axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/item/get-recommend-list`)
    .then((res) => setRecommendItems(resContent(res)));
    const autoSlide = setInterval(moveToNextSlide, 3000);
    return () => clearInterval(autoSlide);
  }, []);

  useEffect(() => {
    if(!localStorage.getItem("token") || localStorage.getItem("token") === undefined) {
        setWishlistItems([]);
        setTotalWishlistItems(0);
    }
    else {
        axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/favorite/get-list/1`,
            {   
                withCredentials: true,
                headers: {"Authorization": `Bearer ${localStorage.getItem("token")}`}
            }
        ).then((res) => {
            setWishlistItems(resContent(res));
            setTotalWishlistItems(resContent(res).length);
        }).catch(() => {
            setWishlistItems([]);
            setTotalWishlistItems(0);
        });
    }
  }, [localStorage.getItem("token")])

  const moveToNextWishlist = () => 
  {
      if (currentWishlist < Math.ceil(totalWishlistItems / itemsPerPage) - 1) 
      {
          setCurrentWishlist((prev) => prev + 1);
      }
  };

  const moveToPrevWishlist = () => 
  {
      if (currentWishlist > 0) 
      {
          setCurrentWishlist((prev) => prev - 1);
      }
  };

  return <>
    <div className="box_Slide">
      <div className="box_item box_slider">
          <div className="slider_track">
              <img
                  src={slides[currentSlide]}
                  alt={`슬라이드 ${currentSlide + 1}`}
                  style={{ display: "block" }}/>
              <button 
                  onClick={moveToPrevSlide} 
                  className="slider_arrow left">
                  &#10094;
              </button>
              <button 
                  onClick={moveToNextSlide} 
                  className="slider_arrow right">
                  &#10095;
              </button>
          </div>
      </div>
    </div>
    <div className="box_Wishlist">
      <div className="text_Wishlist">
          찜 목록
      </div>
      <div className="wishlist_Item">
        {!localStorage.getItem("token") || localStorage.getItem("token") === undefined ?
            <div className="wishlist_Not_Login">
                로그인 후 이용 가능합니다.
            </div> :
          wishlistItems && wishlistItems.length > 0 ? (
              <div className="wishlist">
                  <button 
                      className="wishlist_arrow left"
                      onClick={moveToPrevWishlist}
                      style={{ cursor: currentWishlist === 0 ? 'default' : 'pointer' }}>
                      &#10094;
                  </button>
                  <div className="wishlist_Row">
                      <div className="wishlist_Item_Container">
                          {wishlistItems
                              .slice(currentWishlist * itemsPerPage, (currentWishlist + 1) * itemsPerPage)
                              .map((wishlistItem, idx) => (
                                  <div className="wishlist_Item_Card" key={`wishlistItem${idx}`}>
                                      <img
                                          onClick={() => navigate("/user/item", {state: {itemPk: wishlistItem.item.id}})}
                                          className="wishlist_Item_Image"
                                          src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/03_upload/${wishlistItem.item.img.split("/")[0]}`}
                                          alt="물품 이미지"/>
                                      <div className="wishlist_Item_Title">{wishlistItem.item.title}</div>
                                      <div className="wishlist_Item_Price">{wishlistItem.item.price}원</div>
                                  </div>
                              ))
                          }
                      </div>
                  </div>
                  {currentWishlist < Math.ceil(totalWishlistItems / itemsPerPage) - 1 && (
                      <button 
                          className="wishlist_arrow right"
                          onClick={moveToNextWishlist}>
                          &#10095;
                      </button>
                  )}
              </div>
          ) :
              <div className="wishlist_No_Item">
                  찜 목록을 추가해보세요!
              </div>  
        }
      </div>
    </div>
    <div className="box_Itemlist">
      <div className="text_Recommend">
          추천상품
      </div>
      {recommendItems.length === 0 ? null:
        recommendItems.map((recommendItem, idx) => {
            return <div className="table_Itemlist" key={`recommendItem${idx}`}>
                <img
                    className="itemlist_Item_Image"
                    onClick={() => navigate("/user/item", {state: {itemPk: recommendItem.id}})}
                    src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/03_upload/${recommendItem.img.split("/")[0]}`}
                    alt="물품 이미지"/>
                <div className="itemlist_Item_Title">{recommendItem.title}</div>
                <div className="itemlist Item_Price">{recommendItem.price}원</div>
            </div>;
        })
      }
    </div>
  </>;
}

export default UserMainRecommendComponent;