package ru.yandex.practicum.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.booking.BookingRepository;
import ru.yandex.practicum.booking.BookingStatus;
import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.item.dto.CommentDto;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.item.model.Comment;
import ru.yandex.practicum.item.model.Item;
import ru.yandex.practicum.user.UserService;
import ru.yandex.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentsRepository commentRepository;
    @Mock
    private ItemDtoMapper itemDtoMapper;
    @Mock
    private CommentDtoMapper commentDtoMapper;
    @InjectMocks
    private ItemService itemService;
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;


    @Test
    void addNewItemTest() {
        ItemDto itemDto = ItemDto.builder().id(1L).name("name").description("description").build();
        Item item = new Item();
        Item savedItem = Item.builder().id(1L).build();

        when(itemDtoMapper.convertDtoToItem(1L, itemDto)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(savedItem);
        when(itemDtoMapper.convertItemToDto(savedItem)).thenReturn(itemDto);

        itemService.addNewItem(1L, itemDto);

        verify(userService, atLeast(1)).validateUser(Mockito.anyLong());
        verify(itemDtoMapper, atLeast(1)).convertDtoToItem(Mockito.anyLong(), Mockito.any(ItemDto.class));
        verify(itemRepository, atLeast(1)).save(Mockito.any(Item.class));
        verify(itemDtoMapper, atLeast(1)).convertItemToDto(Mockito.any(Item.class));
    }

    @Test
    void addNewItemCommentTest() {
        Item item = new Item(1L, 1L, "name", "description", true, null);
        User booker = new User(1L, "name", "1@mail.com");
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusHours(2))
                .end(LocalDateTime.now().minusHours(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        CommentDto commentDto = CommentDto.builder().id(1L).authorName("name").text("text")
                .created(LocalDateTime.now()).build();


        when(bookingRepository.findBookingByItemIdAndBookerId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(booking));
        when(commentDtoMapper.toComment(Mockito.any(CommentDto.class), Mockito.any(Booking.class))).thenReturn(new Comment());
        when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(new Comment());
        when(commentDtoMapper.toCommentDto(Mockito.any(Comment.class))).thenReturn(new CommentDto());

        itemService.addNewItemComment(1L, 1L, commentDto);

        verify(bookingRepository).findBookingByItemIdAndBookerId(Mockito.anyLong(), Mockito.anyLong());
        verify(commentDtoMapper).toComment(Mockito.any(CommentDto.class), Mockito.any(Booking.class));
        verify(commentRepository).save(Mockito.any(Comment.class));
        verify(commentDtoMapper).toCommentDto(Mockito.any(Comment.class));
    }

    @Test
    void updateItem() {
        ItemDto itemDto = ItemDto.builder().id(1L).name("name").description("descriptionUpdated").build();
        Item item = Item.builder().id(1L).ownerId(1L).name("name").description("description").build();
        when(itemRepository.getReferenceById(Mockito.anyLong())).thenReturn(item);

        itemService.updateItem(1L, itemDto, 1L);

        verify(userService, atLeast(1)).validateUser(Mockito.anyLong());
        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();
        assertEquals("descriptionUpdated", savedItem.getDescription());
        verify(itemDtoMapper, atLeast(1)).convertItemToDto(Mockito.any(Item.class));
    }

    @Test
    void getItemById() {
        Item item = Item.builder().id(5L).ownerId(1L).name("name").description("description").build();
        ItemDto itemDto = ItemDto.builder().id(5L).name("name").description("description").build();
        User user = new User(1L, "name", "1@mail.com");
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
         when(itemRepository.getReferenceById(5L)).thenReturn(item);
         when(itemDtoMapper.convertItemToDto(item)).thenReturn(itemDto);
         when(bookingRepository.findBookingByItemId(5L)).thenReturn(List.of(booking));

         ItemDto requestedItemDto = itemService.getItemById(5L);

         verify(itemRepository, atLeast(1)).getReferenceById(5L);
         verify(bookingRepository, atLeast(1)).findBookingByItemId(5L);
         assertEquals(booking.getStart(), requestedItemDto.getNextBooking());
    }

    @Test
    void getItemsByOwnerId() {
        Item item1 = new Item(1L, 1L, "name1", "description1", true, null);
        Item item2 = new Item(2L, 1L, "name2", "description2", true, null);
        when(itemRepository.findItemByOwnerId(Mockito.anyLong())).thenReturn(List.of(item1, item2));

        List<ItemDto> requiredItems = itemService.getItemsByOwnerId(1L);

        verify(userService, atLeast(1)).validateUser(1L);
        assertEquals(2, requiredItems.size());
    }

    @Test
    void searchForItems() {
        Item item1 = new Item(1L, 1L, "name1", "description1", true, null);
        Item item2 = new Item(2L, 1L, "name2", "description2", true, null);

        when(itemRepository.getByNameContainingOrDescriptionContainingAllIgnoreCase("123", "123"))
                .thenReturn(List.of(item1, item2));

        List<ItemDto> findedList = itemService.searchForItems("123");

        verify(itemRepository, atLeast(1))
                .getByNameContainingOrDescriptionContainingAllIgnoreCase(Mockito.anyString(), Mockito.anyString());
        assertEquals(2, findedList.size());
    }
}