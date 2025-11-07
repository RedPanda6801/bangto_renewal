import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Modal from 'react-modal';
import './UserSignComponent.css';
import { useNavigate } from 'react-router-dom';

Modal.setAppElement('#root');

const UserSignComponent = () => {
  const [agreeAll, setAgreeAll] = useState("");
  const [agree01, setAgree01] = useState("");
  const [agree02, setAgree02] = useState("");
  const [agree03, setAgree03] = useState("");
  const [agree04, setAgree04] = useState("");
  const [email, setEmail] = useState("");
  const [name, setName] = useState("");
  const [detailAddr, setDetailAddr] = useState("");
  const [phone, setPhone] = useState("");
  const [isPasswordMatch, setIsPasswordMatch] = useState(true);
  const [passwordConfirm, setPasswordConfirm] = useState("");
  const [pw, setPw] = useState("");
  const [keyword, setKeyword] = useState("");
  const [addressList, setAddressList] = useState([]);
  const [selectedAddress, setSelectedAddress] = useState("");
  const [isAddressSearchOpen, setIsAddressSearchOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPage, setTotalPageNum] = useState(1);
  const [visiblePages, setPageNums] = useState([]);
  const [isMembershipOpen, setIsMembershipOpen] = useState(true);
  const navigate = useNavigate();
  
  const handleSign = async () => 
  {
    try 
    {        
      let addr = "";
        
      if(null != selectedAddress)
      {
        addr = `${selectedAddress.roadAddr}!!${detailAddr}!!${selectedAddress.zipNo}`;
        console.log(addr);
      }

      const response = await axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/sign`, 
        {email, pw, name, addr, phone, snsAuth : false}, {withCredentials : true});
      if (response.status == 200) 
      {
        console.log("회원가입 성공");
        alert("회원가입 성공");
        navigate("/login");
      } 
      else 
      {
        console.error("회원가입 실패:");
      }
    } 
    catch (error) 
    {
      console.error("회원가입 오류:", error);
    }
  };

  // 회원가입 약관 창
  useEffect(() => { setIsMembershipOpen(true); }, []);

  // 회원가입 약관 동의 확인
  const isAgreeValid = agreeAll || (agree01 && agree02);

  // 회원가입 약관 전체 동의 토글
  const toggleAgreeAll = () => 
  {
    const agree = agreeAll == "1" ? "" : "1";
    setAgreeAll(agree);
    setAgree01(agree);
    setAgree02(agree);
    setAgree03(agree);
    setAgree04(agree);
  };

  // 개별 동의 체크
  const toggleAgree01 = () => { setAgree01(prev => (prev == "1" ? "" : "1")); };
  const toggleAgree02 = () => { setAgree02(prev => (prev == "1" ? "" : "1")); };
  const toggleAgree03 = () => { setAgree03(prev => (prev == "1" ? "" : "1")); };
  const toggleAgree04 = () => { setAgree04(prev => (prev == "1" ? "" : "1")); };
 
  // 모든 약관 체크 되면 전체 동의 체크
  useEffect(() => 
  {
    if (agree01 == "1" && agree02 == "1" && agree03 == "1" && agree04 == "1") 
    {
      setAgreeAll("1");
    } 
    else 
    {
      setAgreeAll("");
    }
  }, [agree01, agree02, agree03, agree04]);

  // 약관 동의 아이콘 그림 변경
  const getIcon = (agreeValue) => 
    agreeValue == "1" ? "/images/02_icon/icon_02.jpg" : "/images/02_icon/icon_01.jpg";

  // 필수 입력 항목 확인
  const isFormValid = email && pw && passwordConfirm && name && isPasswordMatch;

  // 비밀번호 확인
  useEffect(() => { setIsPasswordMatch(pw == passwordConfirm); }, [pw, passwordConfirm]);
  
  // 페이지 번호 설정
  const updatePageNums = (currentPage, totalPage) =>
  {
    const pages = [];
    const range = 10;
    let start = Math.max(currentPage - 5, 1);
    let end = Math.min(start + range - 1, totalPage);

    if (end - start < range) 
    {
      start = Math.max(end - range + 1, 1);
    }

    for (let i = start; i <= end; i++)
    {
      pages.push(i);
    }
    setPageNums(pages);
  };

  // 주소
  const getAddr = (page = 1) => 
  {
    const apiUrl = `https://business.juso.go.kr/addrlink/addrLinkApiJsonp.do?currentPage=${page}&countPerPage=10&keyword=${encodeURIComponent(keyword)}&confmKey=devU01TX0FVVEgyMDI1MDIwNDEzMzEwMDExNTQ0MzY=&resultType=json&callback=handleApiResponse`;

    window.handleApiResponse = (data) => 
    {
      console.log("API 응답 데이터:", data);
      if (data.results.common.errorCode !== "0") 
      {
        switch(data.results.common.errorCode)
        {
          case "E0005":
            alert("주소가 입력되지 않았습니다.");
            break;
          case "E0006":
            alert("주소를 상세히 입력해주세요");
            break;
          case "E0008":
            alert("주소를 한글자 이상 입력해주세요");
            break;
          case "E0009":
            alert("주소는 숫자만 검색하지못합니다.");
            break;
          case "E0010":
            alert("주소가 너무 깁니다.(한글 40자, 영문, 숫자 80자 이하로 입력)");
            break;
          case "E0011":
            alert("주소에 긴 숫자가 포함되어있습니다.(숫자 10자 이하로 입력)");
            break;
          case "E0012":
            alert("주소는 특수문자 + 숫자만으로는 검색이 불가능합니다.");
            break;
          case "E0013":
            alert("주소는 특수문자(%, =, >, <, [, ])는 검색이 불가능합니다.");
            break;
          case "E0015":
            alert("주소 검색 범위를 초과했습니다.");
            break;
          default:
            alert("주소가 잘못 입력되었습니다.");
            break;
        }
      } 
      else 
      {
        setAddressList(data.results.juso);
        setIsAddressSearchOpen(true);
        setTotalPageNum(Math.ceil(data.results.common.totalCount / 10));
        updatePageNums(page, Math.ceil(data.results.common.totalCount / 10));
      }
    };

    const script = document.createElement('script');
    script.src = apiUrl;
    document.body.appendChild(script);
  };

  // 선택한 주소 저장
  const handleAddressSelect = (address) => 
  {
    setSelectedAddress(address);
    setIsAddressSearchOpen(false);
  };

  // 주소창 페이지 바꾸기
  const handlePageChange = (page) => 
  {
    setCurrentPage(page);
    getAddr(page);
    updatePageNums(page, totalPage);
  };

  return (
    <div className="layout_Sign">
      <div className="box_Sign">
        <Modal
          isOpen={isMembershipOpen}
          onRequestClose={() => { setIsMembershipOpen(false); navigate("/login"); }}
          contentLabel="회원가입 약관 동의 창"
          className="modal_Membership"
          overlayClassName="modal_Overlay_Membership">
          <table className="table_membership">
            <tr
              className="button_Agree"
              onClick={toggleAgreeAll}>
              <td>
                <button>
                  <img src={getIcon(agreeAll)} alt="전체 동의"/>
                </button>
              </td>
              <td>
                전체 동의하기
              </td>
            </tr>
            <tr>
              <td/>
              <td>
                방토샵 이용약관, 개인정보 수집 및 이용, 실명 인증된 아이디로 가입(선택), 위치기반서비스 이용약관(선택) 동의를 포함합니다.
              </td>
            </tr>
            <tr
              className="button_Agree"
              onClick={toggleAgree01}>
              <td>
                <button>
                  <img src={getIcon(agree01)} alt="[필수]이용약관 동의"/>
                </button>
              </td>
              <td>
                [필수] 방토샵 이용약관
              </td>
            </tr>
            <tr>
              <td/>
              <td>
                <div className="membership_text">
                방토샵 서비스 및 제품(이하 ‘서비스’)을 이용해 주셔서 감사합니다.<br/>
                본 약관은 다양한 방토샵 서비스의 이용과 관련하여 방토샵 서비스를 제공하는 방토샵 주식회사
                (이하 ‘방토샵’)와 이를 이용하는 방토샵 서비스 회원(이하 ‘회원’) 또는 비회원과의 관계를 설명하며, 
                아울러 여러분의 방토샵 서비스 이용에 도움이 될 수 있는 유익한 정보를 포함하고 있습니다.<br/>
                방토샵 서비스를 이용하시거나 방토샵 서비스 회원으로 가입하실 경우 여러분은 본 약관 및 관련 운영 
                정책을 확인하거나 동의하게 되므로, 잠시 시간을 내시어 주의 깊게 살펴봐 주시기 바랍니다.<br/>
                다양한 방토샵 서비스를 즐겨보세요.<br/>
                방토샵은 www.bangtoshop.com을 비롯한 방토샵 도메인의 웹사이트 및 응용프로그램(어플리케이션, 앱)을 
                통해 정보 검색, 다른 이용자와의 커뮤니케이션, 콘텐츠 제공, 상품 쇼핑 등 여러분의 생활에 편리함을 
                더할 수 있는 다양한 서비스를 제공하고 있습니다.<br/>
                여러분은 PC, 휴대폰 등 인터넷 이용이 가능한 각종 단말기를 통해 각양각색의 방토샵 서비스를 
                자유롭게 이용하실 수 있으며, 개별 서비스들의 구체적인 내용은 각 서비스 상의 안내, 공지사항, 
                방토샵 웹고객센터(이하 ‘고객센터’) 도움말 등에서 쉽게 확인하실 수 있습니다.<br/>
                방토샵은 기본적으로 여러분 모두에게 동일한 내용의 서비스를 제공합니다. 다만, '청소년보호법' 
                등 관련 법령이나 기타 개별 서비스 제공에서의 특별한 필요에 의해서 연령 또는 일정한 등급을 
                기준으로 이용자를 구분하여 제공하는 서비스의 내용, 이용 시간, 이용 횟수 등을 다르게 하는 등 
                일부 이용을 제한하는 경우가 있습니다. 자세한 내용은 역시 각 서비스 상의 안내, 공지사항, 
                고객센터 도움말 등에서 확인하실 수 있습니다.
                </div>
              </td>
            </tr>
            <tr
              className="button_Agree"
              onClick={toggleAgree02}>
              <td>
                <button>
                  <img src={getIcon(agree02)} alt="[필수]개인정보 수집 및 이용 동의"/>
                </button>
              </td>
              <td>
                [필수] 개인정보 수집 및 이용
              </td>
            </tr>
            <tr>
              <td/>
              <td>
                <div className="membership_text">
                개인정보보호법에 따라 방토샵에 회원가입 신청하시는 분께 수집하는 개인정보의 항목, 
                개인정보의 수집 및 이용목적, 개인정보의 보유 및 이용기간, 동의 거부권 및 동의 거부 시 
                불이익에 관한 사항을 안내 드리오니 자세히 읽은 후 동의하여 주시기 바랍니다.<br/>
                1. 수집하는 개인정보<br/>
                이용자는 회원가입을 하지 않아도 정보 검색, 뉴스 보기 등 대부분의 방토샵 서비스를 
                회원과 동일하게 이용할 수 있습니다. 이용자가 메일, 캘린더, 카페, 블로그 등과 같이 
                개인화 혹은 회원제 서비스를 이용하기 위해 회원가입을 할 경우, 방토샵은 서비스 이용을 
                위해 필요한 최소한의 개인정보를 수집합니다.<br/>
                회원가입 시점에 방토샵이 이용자로부터 수집하는 개인정보는 아래와 같습니다.<br/>
                - 회원 가입 시 필수항목으로 아이디, 비밀번호, 이름, 생년월일, 성별, 휴대전화번호를, 
                선택항목으로 본인확인 이메일주소를 수집합니다. 실명 인증된 아이디로 가입 시, 암호화된 동일인 
                식별정보(CI), 중복가입 확인정보(DI), 내외국인 정보를 함께 수집합니다. 
                만 14세 미만 아동의 경우, 법정대리인의 동의를 받고 있으며, 휴대전화번호 또는 아이핀 인증을 통해 
                법정대리인의 동의를 확인하고 있습니다. 이 과정에서 법정대리인의 정보(법정대리인의 이름, 중복가입확인정보(DI), 
                휴대전화번호(아이핀 인증인 경우 아이핀번호))를 추가로 수집합니다.<br/>
                - 비밀번호 없이 회원 가입 시에는 필수항목으로 아이디, 이름, 생년월일, 휴대전화번호를, 
                선택항목으로 비밀번호를 수집합니다.<br/>
                - 단체 회원가입 시 필수 항목으로 단체아이디, 비밀번호, 단체이름, 이메일주소, 휴대전화번호를, 
                선택항목으로 단체 대표자명을 수집합니다.
                </div>
              </td>
            </tr>
            <tr
              className="button_Agree"
              onClick={toggleAgree03}>
              <td>
                <button>
                  <img src={getIcon(agree03)} alt="[선택]실명 인증된 아이디로 가입 동의"/>
                </button>
              </td>
              <td>
                [선택] 실명 인증된 아이디로 가입 
              </td>
            </tr>
            <tr>
              <td/>
              <td>
                <div className="membership_text">
                실명 인증된 아이디로 가입하시면 본인인증이 필요한 서비스(방토샵 페이, 쇼핑, 멤버십 등)를 가입 후 바로 이용하실 수 있어요.
                </div>
              </td>
            </tr>
            <tr
              className="button_Agree"
              onClick={toggleAgree04}>
              <td>
                <button>
                  <img src={getIcon(agree04)} alt="[선택] 위치기반서비스 이용약관 동의"/>
                </button>
              </td>
              <td>
                [선택] 위치기반서비스 이용약관
              </td>
            </tr>
            <tr>
              <td/>
              <td>
                <div className="membership_text">
                위치기반서비스 이용약관에 동의하시면, 위치를 활용한 광고 정보 수신 등을 포함하는 
                방토샵 위치기반 서비스를 이용할 수 있습니다.<br/>
                제 1 조 (목적)<br/>
                이 약관은 방토샵 주식회사 (이하 “회사”)가 제공하는 위치기반서비스와 관련하여 회사와 
                개인위치정보주체와의 권리, 의무 및 책임사항, 기타 필요한 사항을 규정함을 목적으로 합니다.<br/>
                제 2 조 (약관 외 준칙)<br/>
                이 약관에 명시되지 않은 사항은 위치정보의 보호 및 이용 등에 관한 법률, 개인정보보호법, 
                정보통신망 이용촉진 및 정보보호 등에 관한 법률, 전기통신기본법, 전기통신사업법 등 관계법령과 
                회사의 이용약관 및 개인정보처리방침, 회사가 별도로 정한 지침 등에 의합니다.
                </div>
              </td>
            </tr>
          </table>
          <div className="button_div">
            <button 
              className="button_sign"
              onClick={() =>   setIsMembershipOpen(false)}
              disabled={!isAgreeValid}>
                가입하기
              </button>
            <button 
              className="button_auth"
              onClick={() => { setIsMembershipOpen(false); window.location.href = '/login'; }}>
                로그인화면으로 돌아가기
              </button>
          </div>
        </Modal>
        <table>
          <tr>
            <td>
              <input 
                className="sign_Email" 
                type="email" 
                placeholder="* 이메일"
                value={email}
                onChange={(e) => setEmail(e.target.value)}/>
            </td>
          </tr>
        </table>
        <table>
          <tr>
            <td>
              <input 
                className="sign_Pw" 
                type="password" 
                placeholder="* 비밀번호" 
                value={pw} 
                onChange={(e) => setPw(e.target.value)}/>
            </td>
          </tr>
        </table>
        <table>
          <tr>
            <td>
              <input 
                className="sign_Pw_Ck" 
                type="password" 
                placeholder="* 비밀번호확인" 
                value={passwordConfirm} 
                onChange={(e) => setPasswordConfirm(e.target.value)}/>
            </td>
          </tr>
        </table>
        {!isPasswordMatch && <p className="pW_Ck_Error_Message">비밀번호가 일치하지 않음</p>}
        <table>
          <tr>
            <td>
              <input 
                className="sign_Name" 
                type="text" 
                placeholder="* 별명"
                value={name}
                onChange={(e) => setName(e.target.value)}/>
            </td>
          </tr>
        </table>
        <table>
          <tr>
            <td>
              <input 
                className="sign_Tel" 
                type="tel" 
                placeholder="전화번호"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}/>
            </td>
          </tr>
        </table>
        <table>
          <tr>
            <td>
              <input 
                className="sign_Addr"
                type="text" 
                value={keyword} 
                onChange={(e) => setKeyword(e.target.value)} 
                placeholder="주소 입력"
                onKeyDown={(e) => { if (e.key == 'Enter') { getAddr(1); } }} />
            </td>
          </tr>
        </table>
        <button 
          className="sign_Addr_Search"
          onClick={() => { setCurrentPage(1); getAddr(1)}}>
          검색
        </button>
        <table>
          <tr>
            <td>
              <div 
                className="sign_Addr_Road"
                contentEditable={false}
                tabindex="0">
                {selectedAddress ? selectedAddress.roadAddr : "도로명 주소"}
              </div>
            </td>
          </tr>
          <tr>
            <td>
              <input 
                className="sign_Addr_Detail"
                type="text" 
                value={detailAddr}
                onChange={(e) => setDetailAddr(e.target.value)}
                placeholder="상세 주소"/>
            </td>
          </tr>
          <tr>
            <td>
              <input 
                className="sign_Addr_Zip"
                type="text" 
                value={selectedAddress ? selectedAddress.zipNo : ""} 
                placeholder="우편번호" 
                readOnly/>
            </td>
          </tr>
        </table> 
        <button 
          className="sign_Submit"
          disabled={!isFormValid}
          onClick={() => {handleSign();}}>
          회원가입
        </button>
        {!isFormValid && <p className="info_Ck_Error_Message">* 필수 입력 항목을 작성해주세요</p>}
        <Modal 
          isOpen={isAddressSearchOpen} 
          onRequestClose={() => setIsAddressSearchOpen(false)}
          contentLabel="주소 선택 창"
          className="modal_Addr"
          overlayClassName="modal_Overlay_Addr"
          ariaHideApp={false}>
          <h3>주소 선택</h3>
          <button 
            className="modal_Addr_Close" 
            onClick={() => setIsAddressSearchOpen(false)}>
            X
          </button>
          <table>
            <tr>
              <th>도로명 주소</th>
              <th>우편번호</th>
            </tr>
            {addressList.map((address, index) => (
              <tr 
                key={index} 
                onClick={() => handleAddressSelect(address)}>
                <td className='pointer'>{address.roadAddr}</td>
                <td>{address.zipNo}</td>
              </tr>
            ))}
          </table>
          <div className="pagination_Addr">
            {currentPage > 1 && (
              <button onClick={() => handlePageChange(currentPage - 1)}>
                &lt;
              </button>
            )}
            {visiblePages.map((page) => (
              <button
                key={page}
                onClick={() => handlePageChange(page)}
                className={currentPage == page ? 'active' : ''}>
                {page}
              </button>
            ))}
            {currentPage < totalPage && (
              <button onClick={() => handlePageChange(currentPage + 1)}>
                &gt;
              </button>
            )}
          </div>
        </Modal>
      </div>
    </div>
  );
};

export default UserSignComponent;
