const TimeStamp = (data) => {
  const [date, time] = data.split("T");
  const [year, month, day] = date.split("-");
  const [hour, min, sec] = time.split(":");

  return `${year}년 ${month}월 ${day}일 (${hour}:${min}:${sec})`;
};

const StartTimeFormat = (data) => {
  const date = new Date(`${data}T00:00:00`);
  const dateFormat = new Date(
    date.getTime() - date.getTimezoneOffset() * 60000
  ).toISOString();
  return dateFormat.split(".")[0];
};
const EndTimeFormat = (data) => {
  const date = new Date(`${data}T23:59:59`);
  const dateFormat = new Date(
    date.getTime() - date.getTimezoneOffset() * 60000
  ).toISOString();
  return dateFormat.split(".")[0];
};

const CategoryType = {
  Clothing: "의류",
  Cosmetics: "화장품",
  Electronics: "전자기기",
  Furnitures: "가구",
  Foods: "식품",
  Sports: "스포츠용품",
  Kids: "유아용품",
  Cars: "차량용품",
  Pets: "반려동물 용품",
  Books: "도서",
};

const DeliverType = {
  Preparing: "준비중",
  Delivering: "배송중",
  Delivered: "배송완료",
};

export { TimeStamp, StartTimeFormat, EndTimeFormat, CategoryType, DeliverType };
