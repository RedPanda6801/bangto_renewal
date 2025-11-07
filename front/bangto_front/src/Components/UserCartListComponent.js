import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { resContent } from "./UtilComponent/ResponseData";

const UserCartListComponent = () => {
  const [cartList, setCartList] = useState([]);
  const navigate = useNavigate();

  useEffect(()=> {
    axios
      .get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/cart/get-info`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }).then((res) => {
        const cartList = resContent(res);
        setCartList(cartList);
      }).catch((err) => {
        console.log(err);
        if(err.response.data == "장바구니가 비었습니다."){
          setCartList([]);
        }
      });
  }, [])

  return (
    <div className="box_Cartlist">
      {cartList && cartList.length > 0 ? 
        <div className="cart_Item">
          {
            cartList.map((cart)=>
              <div className="cart_item_div">
                <img className="cart_item_img"
                  src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/03_upload/${cart.item.img.split("/")[0]}`}
                />
                <div style={{textAlign : "center", fontWeight: "bold"}}>{cart.item.title}</div>
              </div>
            )
          }
          <button 
              className="btn_cartlist"
              onClick={() => navigate("/user/cart")}>
              장바구니로이동
          </button>
        </div>
        : <div>장바구니가 비어있습니다.</div>}
    </div>
  );
}

export default UserCartListComponent;