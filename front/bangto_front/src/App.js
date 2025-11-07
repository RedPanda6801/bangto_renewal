import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import ManagerComponent from "./Components/ManagerComponents/ManagerComponent";
import UserMainComponent from "./Components/UserMainComponent";
import UserAuthComponent from "./Components/UserAuthComponent";
import UserSignComponent from "./Components/UserSignComponent";
import StoreComponent from "./Components/SellerComponents/StoreComponent";
import StoreQNADetailComponent from "./Components/StoreQNADetailComponent";
import UserCartComponent from "./Components/UserCartComponent";
import UserPayComponent from "./Components/UserPayComponent";
import UserItemDetailComponent from "./Components/UserItemDetailComponent";
import UserComponent from "./Components/UserComponents/UserComponent";
import UserReviewComponent from "./Components/UserReviewComponent";
import UserGroupItemComponent from "./Components/GroupItemComponents/UserGroupItemComponent";
import UserGroupItemDetailComponent from "./Components/GroupItemComponents/UserGroupItemDetailComponent";
import UserQNAComponent from "./Components/UserQNAComponent";
import ManagerItemInfoComponent from "./Components/ManagerItemInfoComponent";
import UserGroupItemPayComponent from "./Components/GroupItemComponents/UserGroupItemPayComponent";
import "./Components/LayoutComponent.css";
import axios from "axios";
import { useContext, useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { resContent } from "./Components/UtilComponent/ResponseData";
import "bootstrap/dist/css/bootstrap.min.css";
import ScrollToTop from "./Components/UtilComponent/ScrollToTop";

function App() {
  const [userEmail, setUserEmail] = useState(
    localStorage.getItem("USEREMAIL") || ""
  );
  const [userName, setUserName] = useState(
    localStorage.getItem("USERNAME") || ""
  );
  const [token, setToken] = useState(localStorage.getItem("token") || "");
  const navigate = useNavigate();
  const location = useLocation();
  const userMainComponentRef = useRef();

  const rootEmail = process.env.REACT_APP_ROOT_EMAIL;

  useEffect(() => {
    if (!token) {
      localStorage.setItem("USERROLE", "GUEST");
      localStorage.removeItem("USERNAME");
      localStorage.removeItem("USEREMAIL");
      setUserEmail("");
      setUserName("");
    } else {
      axios
        .get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/user/get-info`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        })
        .then((res) => {
          const userContent = resContent(res);
          localStorage.setItem("USEREMAIL", userContent.email);
          localStorage.setItem("USERNAME", userContent.name);
          setUserName(localStorage.getItem("USERNAME") || "");
          setUserEmail(localStorage.getItem("USEREMAIL") || "");

          if (userContent.email === rootEmail) {
            localStorage.setItem("USERROLE", "ADMIN");
            navigate("/manager");
          } else {
            axios
              .get(
                `${process.env.REACT_APP_BACKEND_SERVER_PORT}/seller/get-info`,
                {
                  headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                  },
                }
              )
              .then((res) => {
                localStorage.setItem("USERROLE", "SELLER");
              })
              .catch((err) => {
                localStorage.setItem("USERROLE", "BUYER");
              });
          }
        })
        .catch((err) => {
          //console.log(err);
          //alert("로그인해주세요");localStorage.setItem("USERROLE", "GUEST");
          localStorage.removeItem("token");
          localStorage.setItem("USERROLE", "GUEST");
          localStorage.removeItem("USERNAME");
          localStorage.removeItem("USEREMAIL");
          setUserEmail("");
          setUserName("");
          navigate("/", { state: { category: "Main" } });
        });
    }
  }, [token]);

  useEffect(() => {
    setUserName(localStorage.getItem("USERNAME") || "");
    setUserEmail(localStorage.getItem("USEREMAIL") || "");
  }, []);

  useEffect(() => {
    userMainComponentRef.current?.setCategory(location.state?.category);
  }, [location.state?.category])

  const logout = () => {
    //카카오톡 소셜 로그인 사용자일 경우
    if (localStorage.getItem("kakaoAccessToken") !== null) {
      // 1. 기본 로그아웃
      // axios.post(
      //   "https://kapi.kakao.com/v1/user/logout", {},
      //   {
      //     headers: {
      //       "Authorization": `Bearer ${localStorage.getItem("kakaoAccessToken")}`,
      //     }
      //   }
      // );
      // 2. 카카오 계정과 함께 로그아웃(무슨 차이지?)
      // axios.get(
      //   "https://kauth.kakao.com/oauth/logout",
      //   {
      //     params: {
      //       client_id: process.env.REACT_APP_API_KEY,
      //       logout_redirect_uri: process.env.REACT_APP_LOGOUT_REDIRECT_URI
      //     }
      //   }
      // );
      // 3. 연결 끊기(회원 탈퇴인 듯?)
      axios.post(
        "https://kapi.kakao.com/v1/user/unlink",
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("kakaoAccessToken")}`,
          },
        }
      );
      localStorage.removeItem("kakaoAccessToken");
    }

    localStorage.removeItem("token");
    localStorage.removeItem("USERNAME");
    localStorage.setItem("USERROLE", "GUEST");
    navigate("/", { state: { category: "Main" } });
    setUserEmail("");
    setUserName("");
    setToken("");
  };

  const menuBar = () => {
    return <>
    <div className="header_Menu"
      onClick={() => {
        if (location.pathname !== "/") {
          navigate("/", { state: { category: "Clothing" } });
        }
        if(userMainComponentRef.current) {
          userMainComponentRef.current.setCategory("Clothing");
        }
      }}>의류</div>
      <div className="header_Menu"
      onClick={() => {
        if (location.pathname !== "/") {
          navigate("/", { state: { category: "Cosmetics" } });
        }
        if(userMainComponentRef.current) {
          userMainComponentRef.current.setCategory("Cosmetics");
        }}
      }>화장품</div>
      <div className="header_Menu"
      onClick={() => {
        if (location.pathname !== "/") {
          navigate("/", { state: { category: "Electronics" } });
        }
        if(userMainComponentRef.current) {
          userMainComponentRef.current.setCategory("Electronics");
        }
      }}>전자기기</div>
      <div className="header_Menu"
      onClick={() => {
        if (location.pathname !== "/") {
          navigate("/", { state: { category: "Furnitures" } });
        }
        if(userMainComponentRef.current) {
          userMainComponentRef.current.setCategory("Furnitures");
        }
      }}>가구</div>
      <div className="header_Menu"
      onClick={() => {
        if (location.pathname !== "/") {
          navigate("/", { state: { category: "Foods" } });
        }
        if(userMainComponentRef.current) {
          userMainComponentRef.current.setCategory("Foods");
        }
      }}>식품</div>
      <div className="header_Menu"
      onClick={() => {
        if (location.pathname !== "/") {
          navigate("/", { state: { category: "Sports" } });
        }
        if(userMainComponentRef.current) {
          userMainComponentRef.current.setCategory("Sports");
        }
      }}>스포츠용품</div>
      <div className="header_Menu"
      onClick={() => {
        if (location.pathname !== "/") {
          navigate("/", { state: { category: "Books" } });
        }
        if(userMainComponentRef.current) {
          userMainComponentRef.current.setCategory("Books");
        }
      }}>도서</div>
      <div
        className="header_Menu"
        onClick={() => navigate("/user/groupitem")}
      >
        공동구매
      </div>
    </>;
  };

  return (
    <div className="layout_Main">
      <div className="layout_Header">
        <button
          className="header_Logo"
          onClick={() => {
            if (userEmail === rootEmail) {
              navigate("/manager");
            } else {
              navigate("/", { state: { category: "Main" } });
              if(userMainComponentRef.current) {
                userMainComponentRef.current.setCategory("Main");
              }
            }
          }}
        >
          <img src={`/images/01_logo/logo_01.jpg`} alt="로고 이미지" />
        </button>
        {token !== "" ? (
          userEmail == rootEmail ? (
            <>
              <div className="header_Manager">관리자 메뉴</div>
            </>
          ) : (
            menuBar()
          )
        ) : (
          menuBar()
        )}
        {token !== "" ? (
          userEmail === rootEmail ? (
            <div>
              <div className="header_Manager_Text">관리자님 환영합니다.</div>
              <div className="header_User header_Manager_Menu" onClick={logout}>
                로그아웃
              </div>
            </div>
          ) : (
            <>
              <div>
                <div className="header_User_Text">{userName}님 환영합니다.</div>
                <div className="header_User header_User_Menu" onClick={logout}>
                  로그아웃
                </div>
              </div>
              <div>
                <div
                  className="header_User header_User_Menu_Top"
                  onClick={() => navigate("/seller/apply")}
                >
                  판매자 페이지
                </div>
                <div
                  className="header_User header_User_Menu"
                  onClick={() => navigate("/user/cart")}
                >
                  카트
                </div>
              </div>
              <div>
                <div
                  className="header_User header_User_Menu_Top"
                  onClick={() => navigate("/user")}
                >
                  사용자 페이지
                </div>
              </div>
            </>
          )
        ) : (
          <>
            <div
              className="header_User header_User_First"
              onClick={() => navigate("/login")}
            >
              로그인
            </div>
            <div className="header_User" onClick={() => navigate("/sign")}>
              회원가입
            </div>
          </>
        )}
      </div>
      <ScrollToTop />
      <Routes>
        <Route path="/manager" element={<ManagerComponent />} />
        <Route path="/" element={<UserMainComponent ref={userMainComponentRef} />} />
        <Route
          path="/login"
          element={<UserAuthComponent setToken={setToken} />}
        />
        <Route path="/sign" element={<UserSignComponent />} />
        <Route
          path="/seller/apply"
          element={<StoreComponent setUserName={setUserName} />}
        />
        <Route
          path="/user/cart"
          element={<UserCartComponent setUserName={setUserName} />}
        />
        <Route
          path="/user/pay"
          element={<UserPayComponent setUserName={setUserName} />}
        />
        <Route
          path="/manager/store/info"
          element={<ManagerItemInfoComponent setUserName={setUserName} />}
        />
        <Route
          path="/qna/detail"
          element={<StoreQNADetailComponent setUserName={setUserName} />}
        />
        <Route
          path="/user"
          element={<UserComponent setUserName={setUserName} />}
        />
        <Route
          path="/user/review"
          element={<UserReviewComponent setUserName={setUserName} />}
        />
        <Route
          path="/user/groupitem"
          element={<UserGroupItemComponent setUserName={setUserName} />}
        />
        <Route
          path="/user/item"
          element={<UserItemDetailComponent setUserName={setUserName} />}
        />
        <Route
          path="/user/gorupitem/detail"
          element={<UserGroupItemDetailComponent setUserName={setUserName} />}
        />
        <Route
          path="/user/item/qna"
          element={<UserQNAComponent setUserName={setUserName} />}
        />
        <Route
          path="/group-item/pay"
          element={<UserGroupItemPayComponent setUserName={setUserName} />}
        />
      </Routes>

      <div className="layout_Footer_Buttons">
        <div className="footer_Btn footer_Btn_First">회사소개</div>
        <div className="footer_Btn">개인정보처리방침</div>
        <div className="footer_Btn">이용약관</div>
        <div className="footer_Btn">전자거래분쟁조정위원회</div>
        <div className="footer_Btn">불편사항신고센터</div>
      </div>

      <div className="layout_Footer_Info">
        <table className="footer_Info">
          <tr>
            <td>(주)방토샵</td>
            <td>구매안전서비스(채무자2지급보증)</td>
          </tr>
          <tr>
            <td>
              대표 : 이방토
              <br />
              주소 : 서울특별시 강남구 언주로 508 14층
              <br />
              사업자등록번호 : 000-00-00000 | 통신판매업신고 :
              0000-서울강남구-0000
              <br />
              상담센터 : 0000-0000 | 팩스 : 02-0000-0000
              <br />
              호스팅서비스 : (주)방토샵
            </td>
            <td>
              당사는 하나은행과 재무지급보증 계약을 체결하여 5만원 이상 현금
              등으로 <br />
              결제한 금액에 대해 안전거래를 보장하고 있습니다.
              <br />
              <br />
              COPYRIGHT BANGTOSHOP CO.,LTD.ALL RIGHT RESERVED.
            </td>
          </tr>
        </table>
      </div>
    </div>
  );
}

export default function AppWithRouter() {
  return (
    <BrowserRouter>
      <App />
    </BrowserRouter>
  );
}
