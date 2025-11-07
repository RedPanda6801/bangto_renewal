import "../UserComponent.css";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { resContent, resPage } from "../UtilComponent/ResponseData";
import { useNavigate } from "react-router-dom";
import { TimeStamp } from "../UtilComponent/DataFormat";
import PagenationComponent from "../UtilComponent/PagenationComponent";
const UserQNAInfoComponent = () => {
  const [nowPage, setNowPage] = useState(1);
  const [totalPage, setTotalPage] = useState(1);
  const [qnaList, setQnaList] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get(
        `${process.env.REACT_APP_BACKEND_SERVER_PORT}/qna/my-list/${nowPage}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      )
      .then((res) => {
        const qna = resContent(res);
        console.log(qna);
        setQnaList(qna);
        setTotalPage(resPage(res) ? resPage(res).totalPages : 1);
      })
      .catch((err) => {
        console.log(err);
        alert("로그인 오류");
        navigate(-1);
      });
  }, []);

  return (
    <div className="box_User_QNA">
      <table className="table_User_QNA">
        <tr>
          <td>문의 작성일</td>
          <td>물건 정보</td>
          <td>문의 내용</td>
          <td>답변 내용</td>
          <td>답변 날짜</td>
        </tr>
        {qnaList && qnaList.length > 0 ? (
          qnaList.map((qna) => (
            <tr>
              <td>{TimeStamp(qna.qwriteDate)}</td>
              <td>{qna.itemTitle}</td>
              <td>{qna.qcontent}</td>
              <td>{qna.acontent ? qna.acontent : "답변 없음"}</td>
              <td>{qna.awriteDate ? TimeStamp(qna.awriteDate) : "-"}</td>
            </tr>
          ))
        ) : (
          <tr>문의 사항이 없습니다.</tr>
        )}
      </table>
        <PagenationComponent page={nowPage} setPage={setNowPage} totalPage={totalPage}/>
    </div>
  );
};

export default UserQNAInfoComponent;
