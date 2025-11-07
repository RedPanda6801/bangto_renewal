import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './UserAuthComponent.css'; 
import { useNavigate } from 'react-router-dom';
import {resContent} from './UtilComponent/ResponseData';

const UserAuthComponent = (props) => 
{  
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const code = new URLSearchParams(window.location.search).get("code");
  const navigate = useNavigate();

  const handleAuth = async () => 
  {
    try 
    {
      const response = await axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/login`, 
        {"email":email, "pw":password}, {withCredentials : true});
      
      localStorage.removeItem("token");
      localStorage.setItem("token", resContent(response).token);
      props.setToken(resContent(response).token);
      navigate("/", { state: { category: "Main" } });
    } 
    catch (error) 
    {
      if(error.status === 400){
        alert("잘못된 입력입니다.");
      }else{
        alert("현재 로그인 할 수 없습니다.");
      }
      console.error("로그인 오류:", error);
    }
  };
  
  const kakaoLogin = async () => {
    try {
      const getToken = await axios.post(
        "https://kauth.kakao.com/oauth/token",
        {
          grant_type: "authorization_code",
          client_id: process.env.REACT_APP_API_KEY,
          redirect_uri: process.env.REACT_APP_LOGIN_REDIRECT_URI,
          code: code
        },
        {
          headers: {
            "Content-type": "application/x-www-form-urlencoded;charset=utf-8"
          }
        }
      );
      const accessToken = getToken.data.access_token;
      const getInfo = await axios.get(
        "https://kapi.kakao.com/v2/user/me",
        {
          headers: {
            "Authorization": `Bearer ${accessToken}`,
            "Content-Type": "application/x-www-form-urlencoded;charset=utf-8"
          }
        }
      )
      const info = getInfo;
      const email = info.data.kakao_account.email;
      const name = info.data.kakao_account.profile.nickname
      const SnsSignCheck = await axios.get(
        `${process.env.REACT_APP_BACKEND_SERVER_PORT}/user/get-sns-signed/${email}`
      )
      const isSnsSigned = SnsSignCheck.data.content;
      if(isSnsSigned === -1) {
        const signResponse = await axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/sign`, 
          {email, name, "snsAuth": true}, {withCredentials : true});
        if (signResponse.status === 200) 
        {
          console.log("회원가입 성공");
        } 
        else 
        {
          console.error("회원가입 실패");
        }
      } else if(isSnsSigned === 0) {
        if(window.confirm("이미 가입된 이메일입니다. 카카오톡 계정과 연동하시겠습니까?")) {
          const signResponse = await axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/sign`, 
            {email, name, "snsAuth": true}, {withCredentials : true});
          if (signResponse.status === 200) 
          {
            console.log("계정 연동 성공");
          } 
          else 
          {
            console.error("계정 연동 실패");
          }
        }
        else {
          return;
        }
      }
      const loginResponse = await axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/login`, 
        {"email":email, "snsAuth":true}, {withCredentials : true});
      localStorage.removeItem("token");
      localStorage.setItem("token",resContent(loginResponse).token);
      props.setToken(resContent(loginResponse).token);
      localStorage.setItem("kakaoAccessToken", accessToken);
      navigate("/", { state: { category: "Main" } });
    }
    catch (error) {
      if(error.status === 400){
        alert("잘못된 입력입니다.");
      }else{
        alert("현재 로그인 할 수 없습니다.");
      }
      console.error("로그인 오류:", error);
    }
  }

  useEffect(() => {
    if(code !== null) {
      kakaoLogin();
    }
  }, [code]);

  return (
    <div className="layout_Auth">
      <div className="box_Auth">
        <table>
          <tr>
            <td>
              <input
                className="Auth_Email"
                type="email"
                placeholder="이메일"
                value={email}
                onChange={(e) => setEmail(e.target.value)}/>
            </td>
          </tr>
        </table>
        <table>
          <tr>
            <td>
              <input
                className="Auth_Pw"
                type="password"
                placeholder="비밀번호"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                onKeyDown={(e) => { if (e.key === 'Enter') { handleAuth(); } }}/>
            </td>
          </tr>
        </table>
        {errorMessage && <p className="error_Message">{errorMessage}</p>}
        <input 
          className="btn_Auth"
          type="button" 
          value="로그인"
          onClick={handleAuth}/>
        <a href={`https://kauth.kakao.com/oauth/authorize?client_id=${process.env.REACT_APP_API_KEY}&redirect_uri=${process.env.REACT_APP_LOGIN_REDIRECT_URI}&response_type=code`}>
          <img
            className="btn_Social"
            src={`/images/02_icon/kakao_login_medium_narrow.png`} 
            alt="카카오 로그인" />
        </a>
        <div
          className="btn_Join"
          onClick={() => (navigate("/sign"))}>
          회원가입
        </div>
      </div>
    </div>      
  );
}

export default UserAuthComponent;